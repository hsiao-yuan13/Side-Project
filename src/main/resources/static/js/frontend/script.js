$(function() {
	const token = $("meta[name='_csrf']").attr("content");
	const header = $("meta[name='_csrf_header']").attr("content");

	if (token && header) {
		$(document).ajaxSend(function(e, xhr) {
			xhr.setRequestHeader(header, token);
		});
		console.log("csrf token已設置");
	}else{
		console.warn("未找到csrf token");
	}

});

$(document).ready(function() {


	// 測試是否能找到元素
	console.log("logoutLink 存在:", $("#memLogoutLink").length);
	console.log("logoutForm 存在:", $("#memLogoutForm").length);
});

$(document).on("click", "#memLogoutLink", function(e) {
	console.log("點擊了會員登出");
	e.preventDefault();
	$("#memLogoutForm").submit();
});

$(document).on("click", "#sellerLogoutLink", function(e) {
	console.log("點擊了商家登出");
	e.preventDefault();
	$("#sellerLogoutForm").submit();
});
