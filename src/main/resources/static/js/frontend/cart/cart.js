/**購物車操作
 * 
 */
$(document).ready(function() {
//	console.log(sessionStorage.getItem("memId"));
	var memId = sessionStorage.getItem("memId"); // 取得 memId

    if (!memId) {
        console.error("購物車獲取失敗：memId 不存在，請先登入");
        return;
    }

    getCart();
});

//點擊加入購物車按鈕
$(document).on("click",  ".add-to-cart", function(){
	let productId = $("#productId").val();
	let productName = $("#productName").val();
//	let productPic = $("#productPic").val();
	let productPrice = $("#productPrice").val();
	let productQty = $("#quantityInput").val();
	let sellerId = $("#sellerId").val();
	
	//商品加入購物車
	$.ajax({
		url: "/frontend/cart/addToCart",
		method: "POST",
		contentType: "application/json",
		data: JSON.stringify({ 
			memId: Number(memId),
			productId: Number(productId),
			productName: productName,
//			productPic: productPic,
			productQty: Number(productQty),
			productPrice: Number(productPrice),
			sellerId:Number(sellerId)
			}),
		dataType: "text",
		success: function(response){
			alert("商品已加入購物車");
			getCart();
		},
		error: function(error){
			alert("加入購物車失敗,請稍後再試");
		}
	});
});




function updateCartCountBadge(){
	let totalQty = 0;
	
	$(".quantity-input").each(function(){
		totalQty += parseInt($(this).val());
	});
	
	$(".cart-count-badge").text(totalQty);
}





function updateCartUI(cartData){
	let cartContainer = $("#cart-table-body");
	cartContainer.empty();
	
	
	if(cartData.length === 0){
		cartContainer.html("<p>購物車內沒有商品</p>");
		return;
	}else{
		
		cartData.forEach(item => {
			let sub_total = item.productPrice * item.productQty;
			let productRow = `
				<tr class="cart-item" data-product-id="${item.productId}">
					<td><img src="/reader/ProductGifReader?productId=${item.productId}" alt="Image" class="product-image"></td>
	            	<td><span class="product-name">${item.productName}</span></td>
	            	<td><span class="product-price">${item.productPrice}</span></td>    
	            	<td><input type="number" class="quantity-input" value="${item.productQty}" min="1">
		                <button class="update-qty" data-product-id="${item.productId}" data-change="-1">-</button>
		                <button class="update-qty" data-product-id="${item.productId}" data-change="1">+</button></td>
		            <td><span class="sub-total">${sub_total}</span></td>    
	                <td><button class="remove-product" data-product-id="${item.productId}">移除</button></td>
            </tr>
			`;
			cartContainer.append(productRow);
		});
	}
	
}


//顯示購物車
function getCart(){
	$.ajax({
		url: "/frontend/cart/showCart",
		method: "GET",
		data: {
			memId: memId
		},
		success: function(response){
//			console.log("伺服器回傳資料:", response);
			
			let cartArray = Object.entries(response).map(([productId, productData]) => ({
				productId: productData.productId,
				productName: productData.productName,
				productPrice: productData.productPrice,
				productQty: productData.productQty,
			}));
			
			updateCartUI(cartArray);
			
//			購物車商品數量更新
			let totalQty = cartArray.reduce((sum, item) => sum + item.productQty, 0);
			$(".cart-count-badge").text(totalQty);
		},
		error: function(xhr, status, error){
//			console.error("獲取購物車失敗", error);
//			console.log(xhr);
//			console.error("詳情: ", xhr.responseText);
//			console.log("發送 AJAX，memId:", memId); // 先確認 memId 有值
		}
	});
	
};



//點擊商品數量按鈕
$(document).on("click", ".update-qty", function(){
	let productId = $(this).data("product-id");
    let change = $(this).data("change"); // +1 或 -1
    let quantityInput = $(this).siblings(".quantity-input");
    
    let newQty = parseInt(quantityInput.val()) + change;
    if (newQty < 0) newQty = 0; // 最低數量是 0
    
    let row = $(this).closest("tr");
    let productPrice = parseFloat(row.find(".product-price").text());
    let subPriceElement = row.find(".sub-total")
    
    let newSubTotal = productPrice * newQty;
    
  //購物車內商品數量更新  
    $.ajax({
		url: "/frontend/cart/updateQty",
		method: "POST",
		contentType: "application/json",
		data: JSON.stringify({
			memId: memId,
			productId: productId,
			productPrice: productPrice,
			productQty: newQty
			}),
		dataType: "text",
		success: function(response){
			if (newQty === 0) {
                alert("商品已移除購物車");
                getCart();
            } else {
                quantityInput.val(newQty); // 更新前端數量顯示
                subPriceElement.text(newSubTotal);
                
//                更新購物車總商品數量
                updateCartCountBadge();
            }
		},
		error: function(error){
			console.error("更新購物車失敗", error);
			alert("商品數量更新失敗");
		}
	});
});




//移除購物車商品
$(document).on("click", ".remove-product", function(){
	let productId = $(this).data("product-id");
	
	$.ajax({
		url: "/frontend/cart/removeFromCart",
		method: "DELETE",
		contentType: "application/json",
		data: JSON.stringify({
			memId: memId,
			productId: productId
		}),
		success: function(){
			alert("商品移除成功");
			getCart();
		},
		error: function(){
			alert("商品移除失敗");
		}
	});
});
	





//清空購物車
$(document).on("click", ".delete-cart", function(){	
	
	$.ajax({
		url: "/frontend/cart/clearCart",
		method: "DELETE",
		success: function(){
			alert("購物車已清空");
		},
		error: function(){
		alert("購物車清空失敗");
		}
	});
});