<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="shops">

    <h2>Order Information</h2>


    <table class="table table-striped">
   		<tr>
            <th>Name</th>
            <td><c:out value="${order.name}"/></td>
        </tr>
        <tr>
            <th>Supplier</th>
            <td><c:out value="${order.supplier}"/></td>
        </tr>
        <tr>
            <th>Product</th>
            <td><c:out value="${order.product.name}"/></td>
        </tr>
        <tr>
            <th>Number of products</th>
            <td><c:out value="${order.productNumber}"/></td>
        </tr>
        <tr>
            <th>Order Date</th>
            <td><c:out value="${order.orderDate}"/></td>
        </tr>
         <tr>
            <th>Order Status</th>
            <td><c:out value="${order.orderStatus}"/></td>
        </tr>
         <tr>
            <th>Shop</th>
            <td><c:out value="${order.shop.name}"/></td>
        </tr>
    </table>
    <c:if test="${order.orderStatus == 'INPROCESS' }">
    	<a href="/orders/${order.id}/received" class="btn btn-default" >Order Received</a>
	</c:if>
</petclinic:layout>
