$(document).ready(function() {
	let stompClient = null;
	let currentUser = null;
	let currentRoomId = null;
	let activeChat = null;
	let tempRoom = [];

	function formatTimestamp(timestamp) {
		const msgDate = new Date(timestamp);

		return msgDate.toLocaleDateString("zh-TW", {
			hour: "2-digit",
			minute: "2-digit"
		});
	}
	

	function getCurrentUser() {
		$.ajax({
			url: "/frontend/chat/currentUser",
			type: "GET",
			async: false,
			success: function(data){
				currentUser = data;
			},
			
		error: function(err){
			console.error("取得登入使用者失敗", err);
			currentUser = null;
			}
		});
	}


	async function initChat() {
		getCurrentUser();
		$.get('/frontend/chat/checkLogin', function(isLoggedIn) {
			if (isLoggedIn) {
				loadRoomList();
				console.log("聊天室系統初始化成功");
			} else {
				console.log("使用者未登入");
			}
		});
	}


	function loadRoomList(selectRoomId) {
		console.log("Loading room List...");

		$.get("/frontend/chat/roomList", function(roomList) {
			renderRoomList(roomList, selectRoomId);
		}).fail(function(xhr, status, error) {
			console.error("fail to load chat roomList", error);
			$("#myChatList").html('<div class="error">fail to load chat roomList</div>');
		});
	}


	function renderRoomList(roomList, selectRoomId) {
		const listContainer = $('#myChatList');
		listContainer.empty();

		const allRooms = mergeRoomLists(roomList, tempRoom);

		if (allRooms.length === 0) {
			listContainer.append('<div class="no_chat">暫無聯絡人<div>');
			return;
		}

		allRooms.forEach(room => {
			const receiverIdAttr = room.receiverId != null ? `data-receiver-id="${room.receiverId}"` : '';
			const receiverRoleAttr = room.receiverRole != null ? `data-receiver-role="${room.receiverRole}"` : '';

			const chatItem = `
				<div class="chatListTag" 
				data-room-id="${room.roomId}" 
				${receiverIdAttr} ${receiverRoleAttr}
				>
            <div class="head"><img src="" alt=""></div>
            <div class="mytext">
              <div class="receiver-name">${room.receiverName}</div>
              <div class="dec">${room.lastMsg || ''}</div>
            </div>
          </div>
				`;
			listContainer.append(chatItem);
		});

		console.log("房間渲染完成");

		if (selectRoomId) {
			const targetRoom = $(`.chatListTag[data-room-id="${selectRoomId}"]`);
			if(targetRoom.length){
				targetRoom.click();
			}
		}
	}


	function mergeRoomLists(backendRooms, tempRooms) {
		const roomMap = new Map();
		backendRooms.forEach(room => {
			roomMap.set(room.roomId, room);
		});

		tempRooms.forEach(room => {
			if (!roomMap.has(room.roomId)) {
				roomMap.set(room.roomId, room);
			}
		});

		return Array.from(roomMap.values());
	}




	$(document).on('click', '.chatListTag', function() {

		currentRoomId = $(this).data('room-id');;
		activeChat = $(this);

		$('.chatListTag').removeClass('active');
		activeChat.addClass('active');


		openChatBox();
		connectRoom(currentRoomId);

	});


	function connectRoom(roomId) {
		if (stompClient && stompClient.connected) {
			stompClient.disconnect(() => {
				subscribeRoom(roomId);
			});
		} else {
			subscribeRoom(roomId)
		}

	}

	function subscribeRoom(roomId) {

		const socket = new SockJS("/ws");
		stompClient = Stomp.over(socket);

		stompClient.connect({}, function(frame) {
			stompClient.subscribe(`/topic/${roomId}`, function(message) {
				const chat = JSON.parse(message.body);
				const isSelf = currentUser && chat.senderId === currentUser.id;
				const type = isSelf ? "you-message" : "other-message";
				const displayname = isSelf ? `我 (（${chat.senderRole})` : `${chat.senderName}`;
				appendMessage(chat, type, displayname);

			});
			loadHistory(roomId);
			console.log("WebSocket connected:", frame);

		});
	}



	function loadHistory(roomId) {
		$("#chatRoom").html("");
		$.get(`/frontend/chat/history/${roomId}`, function(messages) {

			if (!messages || messages.length === 0) {
				$("#chatRoom").append('<div class="no_chat">暫無聊天紀錄</div>');
				return;
			}

			messages.forEach(chat => {
				const type = chat.senderId === currentUser.id ? "you-message" : "other-message";
				const isSelf = currentUser && chat.senderId === currentUser.id;
				const displayname = isSelf ? `我 (（${chat.senderRole})` : `${chat.senderName}`;

				appendMessage(chat, type, displayname);
			});
		});
	}





	function appendMessage(chat, type) {
		const timeStr = formatTimestamp(chat.timestamp);

		const msgHtml = `
	<div class="message_row ${type}">
		
		<div class="message-content">
		    
			<div class="message-text">${chat.content}</div>
			<div class="message-time">${timeStr}</div>
		</div>
	</div>`;

		$("#chatRoom").append(msgHtml);
		$("#chatRoom").scrollTop($("#chatRoom")[0].scrollHeight);


	}
	
	function getReceiverInfo(roomId, callback){
		$.get(`/frontend/chat/receiverInfo?roomId=${roomId}`, function(receiverInfo){
			callback(receiverInfo);
		}).fail(function(){
			console.log("無法從後端取得receiverInfo");
			callback(null);
		});
	}


	function sendMessage() {
		if (!currentRoomId) {
			return
		}

		const msg = $(".sendMsg").val().trim();

		if (!msg) {
			return;
		}

		getReceiverInfo(currentRoomId, function(receiverInfo){
			if (!receiverInfo.receiverId) {
			console.error("找不到receiver");
			alert("發送失敗");
			return;
		}
		
		const chat = {
			roomId: currentRoomId,
			receiverId: receiverInfo.receiverId,
			receiverRole: receiverInfo.receiverRole,
			receiverName: receiverInfo.receiverName,
			content: msg,
		};
		
		console.log("送出的 chat = ", chat);
		stompClient.send(`/app/chat.send`, {}, JSON.stringify(chat));

		$(".sendMsg").val("");
		});

	}


	

	



	$('#chatWithSeller').on('click', function() {
		const receiverId = $(this).data('seller-id');
		const receiverName = $(this).data('seller-name');

		$.get('/frontend/chat/checkLogin', function(isLoggedIn) {
			if (!isLoggedIn) {
				alert("無法傳送訊息，請先登入");
				window.location.href = "/frontend/login/memLoginPage";
				return;
			} else {
				console.log("私訊商家已點擊");


				$.get(`/frontend/chat/roomId?receiver=${receiverId}`, function(roomId) {

					if (!tempRoom.some(r => r.roomId === roomId)) {
						tempRoom.push({
							roomId: roomId,
							receiverId: receiverId,
							receiverRole: "ROLE_SELLER",
							receiverName: receiverName,
							lastMsg: ""
						});
					}

					loadRoomList(roomId);
					openChatBox();
					connectRoom(roomId);
					console.log('取得roomId:', roomId);

				});
			}
		});

	});


	function openChatBox() {
		$('.chat_container').show();

	}



	$('.chat_button').on('click', function() {
		$('.chat_container').toggle();
		$('.chat_button').toggleClass('active');

		loadRoomList();
	});


	$('.send_icon').on('click', function() {
		sendMessage();
	});



	$('.sendMsg').on('keypress', function(e) {
		if (e.which === 13) {
			sendMessage();
		}
	});


	$('.chat_container').hide();

	initChat();


});












