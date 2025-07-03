$(document).ready(function() {
	var memId = sessionStorage.getItem("memId"); // 取得 memId

    if (!memId) {
        console.error("訂單商品清單獲取失敗：memId 不存在，請先登入");
        return;
    }

    getCheckoutList(memId);
});

function updateCheckoutUI(checkoutGroupData){
	let checkoutContainer = $("#checkout-items");
	checkoutContainer.empty();
		
	if(Object.keys(checkoutGroupData).length === 0){
		checkoutContainer.html("<p>購物車內沒有商品</p>");
		return;
	}else{
		
		Object.entries(checkoutGroupData).forEach(([sellerId, checkoutGroup]) => {
			let sellerName = checkoutGroup.sellerName;
			let items = checkoutGroup.items;

			let checkoutGroupHTML = `
				<div class="seller-group" id="group-${sellerId}">
					<h4>商家 : ${sellerName}</h4>
					<table class="checkout-table">
						<thead>
					        <tr>
					        	<th class="pic-col"></th>
					            <th class="name-col">商品名稱</th>
					            <th class="quantity-col">數量</th>
					            <th class="price-col">價格</th>
					            <th class="sub-total-col">小計</th>
					        </tr>
					    </thead>
					    <tbody>
			`;
			
			let sellerTotal = 0;
			
			items.forEach(item => {
				let sellerSubtotal = item.productQty * item.productPrice;
				sellerTotal += sellerSubtotal;
			

			checkoutGroupHTML += `
				<tr class="checkout-item" data-productId="${item.productId}">
					<td><img src="/reader/ProductGifReader?productId=${item.productId}" alt="Image" class="product-image"></td>
	            	<td><span class="productName">${item.productName}</span></td>
	            	<td><span class="quantity">${item.productQty}</span></td>    
	            	<td><span class="productPrice">${item.productPrice}</span></td>    
		            <td><span class="subTotal">${sellerSubtotal}</span></td>    
            </tr>
			`;
			
		});
		
		checkoutGroupHTML += `
				</tbody>
			</table>
			
			<div class="checkout-ready">
				<span class="sellerTotal-col">合計 : </span><span class="sellerTotal">${sellerTotal}</span>
				<br>
				<button class="checkout-submit" data-seller-id="${sellerId}">結帳</button>
			</div>	
		</div>	
		`;
		
		checkoutContainer.append(checkoutGroupHTML);

	});
  }
}

function getCheckoutList(memId){
	$.ajax({
		url: "/frontend/cart/checkoutList",
		method: "GET",
		data: {
			memId: memId
		},
		success: function(response){
			console.log("伺服器回傳資料:", response);
			updateCheckoutUI(response);
		},
		error: function(xhr, status, error){
			console.error("獲取購物車失敗", error);
			console.log(xhr);
			console.error("詳情: ", xhr.responseText);
		}
	});
}

// 綁定結帳按鈕事件，放在最外層避免重複綁定
$(document).on("click", ".checkout-submit", function(){
	let checkoutItems = "";
	let sellerId = $(this).data("seller-id");
	let $group = $(`#group-${sellerId}`);
	let totalAmount = 0;

	$group.find(".checkout-item").each(function(){
		let productName = $(this).find(".productName").text().trim();
		let productQty = parseInt($(this).find(".quantity").text().trim());
		let productPrice = parseInt($(this).find(".productPrice").text().trim());
		let subtotal = productQty * productPrice;
		
		totalAmount += subtotal;
		checkoutItems += `${productName} * ${productQty}件#`;
	});

	checkoutItems = checkoutItems.slice(0,-1);

	$.ajax({
		url: "/frontend/cart/ecpayCheckout",
		method: "POST",
		contentType: "application/json",
		data: JSON.stringify({
			itemName: checkoutItems,
        	totalAmount: totalAmount,
        	sellerId: sellerId
		}),
		success: function(response){
			$("body").append(response);
			$("form").last().submit();
		},
		error: function(){
			alert("ECPay 結帳請求失敗！");
		}
	});
});
