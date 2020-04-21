<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<petclinic:layout pageName="shops">

    <h2>Shop Information</h2>

    <table id="productsTable" class="table table-striped">
        <thead>
        <tr>
        	<th>Name</th>
            <th>Price</th>
            <th>Stock</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${products}" var="product">
            <tr>
                <td>
                    <spring:url value="/shops/1/products/{productId}" var="productUrl">
                        <spring:param name="productId" value="${product.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(productUrl)}"><c:out value="${product.name}"/></a>
                </td>
                 <td>
                    <c:out value="${product.price}"/>
                </td>
                <td>
                    <c:out value="${product.stock}"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
   
    
   	<sec:authorize access="hasAuthority('admin')">
   	<a href="/shops/1/products/new" class="btn btn-default">Add Product</a>
   	<br>
   	<br>
    <table id="ordersTable" class="table table-striped">
        <thead>
        <tr>
        	<th>Name</th>
            <th>Product name</th>
            <th>Number</th>
            <th>Date</th>
            <th>Status</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${orders}" var="order">
            <tr>
                <td>
                    <spring:url value="/shops/1/orders/{orderId}" var="orderUrl">
                        <spring:param name="orderId" value="${order.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(orderUrl)}"><c:out value="${order.name}"/></a>
                </td>
                 <td>
                    <spring:url value="/shops/1/products/{productId}" var="productUrl">
                        <spring:param name="productId" value="${order.product.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(productUrl)}"><c:out value="${order.product.name}"/></a>
                </td>
                <td>
                    <c:out value="${order.productNumber}"/>
                </td>
                <td>
                    <petclinic:localDate date="${order.orderDate.toLocalDate()}" pattern="yyyy/MM/dd" /><c:out value=" ${order.orderDate.toLocalTime()}" />
				</td>
                <td>
                    <c:out value="${order.orderStatus}"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <a href="/shops/1/orders/new" class="btn btn-default">Add Order</a>
    <br>
    <br>
    <a href="/shops/1/edit" class="btn btn-default">Edit shop</a>
    </sec:authorize>
</petclinic:layout>
