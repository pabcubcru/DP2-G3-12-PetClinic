<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="shops">
    <h2>
       New Order
    </h2>
    <form:form modelAttribute="order" class="form-horizontal" id="add-order-form">
        <div class="form-group has-feedback">
       		<petclinic:inputField label="Name" name="name"/>
            <petclinic:inputField label="Supplier" name="supplier"/>
            <petclinic:inputField label="Number of products" name="productNumber"/>
            <petclinic:selectField label="Product" name="product.name" size="${products.size()}" names="${products}"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10" align="right">
               <button class="btn btn-default" type="submit">Create</button>
            </div>
        </div>
    </form:form>
</petclinic:layout>