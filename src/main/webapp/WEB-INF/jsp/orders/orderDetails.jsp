<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="orders">

	<h2>Order Information</h2>


	<table class="table table-striped">
		<tr>
			<th>Name</th>
			<td><c:out value="${order.name}" /></td>
		</tr>
		<tr>
			<th>Supplier</th>
			<td><c:out value="${order.supplier}" /></td>
		</tr>
		<tr>
			<th>Product</th>
			<td><spring:url value="/shops/1/products/{productId}" var="productUrl">
					<spring:param name="productId" value="${order.product.id}" />
				</spring:url> <a href="${fn:escapeXml(productUrl)}"><c:out
						value="${order.product.name}" /></a></td>
		</tr>
		<tr>
			<th>Number of products</th>
			<td><c:out value="${order.productNumber}" /></td>
		</tr>
		<tr>
			<th>Order Date</th>
			<td><c:out value="${order.orderDate}" /></td>
		</tr>
		<tr>
			<th>Order Status</th>
			<td><c:out value="${order.orderStatus}" /></td>
		</tr>
		<tr>
			<th>Shop</th>
			<td>
                    <spring:url value="/shops/1" var="shopUrl">
                        <spring:param name="shopId" value="${order.shop.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(shopUrl)}"><c:out value="${order.shop.name}"/></a>
                </td>
		</tr>
	</table>
	<c:if test="${order.orderStatus == 'INPROCESS' }">
		<a href="/shops/1/orders/${order.id}/received" class="btn btn-default">Order
			Received</a>
	</c:if>
</petclinic:layout>