<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="orders">
    <h2>Orders</h2>

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
                    <spring:url value="/orders/{orderId}" var="orderUrl">
                        <spring:param name="orderId" value="${order.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(orderUrl)}"><c:out value="${order.name}"/></a>
                </td>
                 <td>
                    <c:out value="${order.product.name}"/>
                </td>
                <td>
                    <c:out value="${order.productNumber}"/>
                </td>
                <td>
                    <c:out value="${order.orderDate}"/>
                </td>
                <td>
                    <c:out value="${order.orderStatus}"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <a href="/orders/new" class="btn btn-default">Add Order</a>
</petclinic:layout>
