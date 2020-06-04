<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<petclinic:layout pageName="shops">

	<h2>Product Information</h2>

	<table class="table table-striped">
		<tr>
			<th>Name</th>
			<td><c:out value="${product.name}" /></td>
		</tr>
		<tr>
			<th>Price <c:if test="${activeDiscount}">With Discount</c:if>
				(EUR)
			</th>
			<td><c:if test="${activeDiscount}">
					<c:out value="${priceWithDiscount}" />
				</c:if> <c:if test="${!activeDiscount}">
					<c:out value="${product.price}" />
				</c:if></td>
		</tr>
		<tr>
			<th>Stock</th>
			<td><c:out value="${product.stock}" /></td>
		</tr>
		
	</table>
	<c:if test="${product.discount != null}">
		<h2>Discount Information</h2>
		<table class="table table-striped">
			<tr>
				<th>Percentage</th>
				<td><c:out value="${product.discount.percentage}" /></td>
			</tr>
			<tr>
				<th>Start Date</th>
				<td><petclinic:localDate date="${product.discount.startDate}"
						pattern="yyyy/MM/dd" /></td>
			</tr>
			<tr>
				<th>Finish Date</th>
				<td><petclinic:localDate date="${product.discount.finishDate}"
						pattern="yyyy/MM/dd" /></td>
			</tr>
		</table>
	</c:if>
	<sec:authorize access="hasAuthority('admin')">
		<c:if test="${product.discount == null}">
			<a
				href="/shops/${product.shop.id}/products/${product.id}/discounts/new"
				class="btn btn-default">Create Discount</a>
		</c:if>
		<c:if test="${product.discount != null}">
			<a
				href="/shops/${product.shop.id}/products/${product.id}/discounts/${product.discount.id}/edit"
				class="btn btn-default">Update Discount</a>
			<a
				href="/shops/${product.shop.id}/products/${product.id}/discounts/${product.discount.id}/delete"
				class="btn btn-default">Delete Discount</a>
		</c:if>
		<a href="/shops/${product.shop.id}/products/${product.id}/edit"
			class="btn btn-default">Update Product</a>
		<c:if test="${canDeleteIt}">
			<a href="/shops/${product.shop.id}/products/${product.id}/delete"
				class="btn btn-default">Delete Product</a>
		</c:if>
		<c:if test="${!canDeleteIt}">
			<br>
			<br>
			<c:out
				value="This product can not be deleted because it has realized at least one order with it."></c:out>
		</c:if>
	</sec:authorize>
</petclinic:layout>
