<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <link href="webjars/bootstrap/5.1.3/css/bootstrap.min.css" ref="stylesheet">
    <title>Add Todos Page</title>

</head>
<body>

<div class="container">
    <h1>Enter Todo Details</h1>
    <form:form method="post" modelAttribute="todo">

        <fieldset class="mb-3">
            <form:label path="description">Description</form:label>
            <form:input path="description" type="text" class="form-control"/>
            <form:errors path="description" cssClass="text-danger"/>
        </fieldset>

        <form:input type="hidden" path="id"/>
        <form:input type="hidden" path="done"/>
        <form:input type="hidden" path="targetDate"/>

        <button type="submit" class="btn btn-success">Summit</button>

    </form:form>
</div>
<script src="webjars/bootstrap/5.1.3/js/bootstrap.min.js"></script>
<script src="webjars/jquery/3.6.0/jquery.min.js"></script>
<script src="webjars/popper.js/2.10.2/umd/popper.min.js"></script>
<script src="webjars/bootstrap/5.1.3/js/bootstrap.bundle.min.js"></script>
<link href="webjars/bootstrap/5.1.3/css/bootstrap.min.css" rel="stylesheet">


</body>
</html>
