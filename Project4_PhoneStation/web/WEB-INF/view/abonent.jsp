<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="page-header"><h1><fmt:message key="${pageCaption}"/></h1></div>

<c:if test="${data==null}">
    <h3><fmt:message key='entryAbonentWelcome'/></h3>
</c:if>
    
<c:if test="${data=='actionResult'}">
    <h3><fmt:message key='${pageText}'/></h3>
</c:if>
    
<c:if test="${(data=='services')||(data=='myServices')}">
    
    <form action="abonent" method="post">
    <table class="table table-hover">
        <thead>
            <tr>
                <th>V</th>
                <th><fmt:message key="thName"/></th>
                <th><fmt:message key="thPrice"/></th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="serviceEntry" items="${servicesList}">
            <tr>
                <td><input type="checkbox" name="checked_services" value="${serviceEntry.id}"></td>
                <td><ch:displayNameSelector lang="${language}" obj="${serviceEntry}"/></td>
                <td>${serviceEntry.price}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <input type ="hidden" name="section" value="services">
    <c:if test="${data=='services'}">
        <button class="btn btn-success" type="submit" name="action" value="enableServices"><fmt:message key="btnEnableSelected"/></button>
    </c:if>
    <c:if test="${data=='myServices'}">
        <button class="btn btn-success" type="submit" name="action" value="disableServices"><fmt:message key="btnDisableSelected"/></button>
    </c:if>
    </form>
</c:if>
<c:if test="${(data=='bills')}">
        <form action="abonent" method="post">
        <fieldset>
        <div class="row">
            <div class="col-md-3">
                <label class="control-label" for="startDate"><fmt:message key='lblStartDate'/></label>
                <input value="${startDate}" name="startDate" placeholder="<fmt:message key='plhDate'/>" class="input-xlarge" type="text">
            </div>
            <div class="col-md-3">
                <label class="control-label" for="endDate"><fmt:message key='lblEndDate'/></label>
                <input value="${endDate}" name="endDate" placeholder="<fmt:message key='plhDate'/>" class="input-xlarge" type="text">
            </div>  
            <div class="col-md-3">
                <button class="btn btn-success" type="submit" name="action" value="${showAction}"><fmt:message key="btnUpdateView"/></button>
            </div>  
            <input type="hidden" name="section" value ="bills">
        </div>
        </fieldset>
        </form>
        <div class="container">
        <table class="table table-hover">
        <thead>
            <tr>
                <th><fmt:message key="thDate"/></th>
                <th><fmt:message key="thDateIssued"/></th>
                <th><fmt:message key="thAmount"/></th>
                <th><fmt:message key="thPay"/></th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="billEntry" items="${billsList}">
            <tr>
                <td>${billEntry.dateIssued}</td>
                <td>${billEntry.payMonth}</td>
                <td>${billEntry.amount}</td>
                <td>
                    <c:if test="${billEntry.isPayed!=true}">
                        <form action="abonent" method="post">
                            <input type="hidden" name ="billId" value="${billEntry.id}">
                            <input type="hidden" name="section" value ="bills">
                            <button class="btn btn-success" type="submit" name="action" value="payBill"><fmt:message key="btnPayBill"/></button>
                        </form> 
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
        </div>    
</c:if>
<c:if test="${(data=='callsListing')}">
    <form action="abonent" method="post">
        <fieldset>
        <div class="row">
            <div class="col-md-3">
                <label class="control-label" for="startDate"><fmt:message key='lblStartDate'/></label>
                <input value="${startDate}" name="startDate" placeholder="<fmt:message key='plhDate'/>" class="input-xlarge" type="text">
            </div>
            <div class="col-md-3">
                <label class="control-label" for="endDate"><fmt:message key='lblEndDate'/></label>
                <input value="${endDate}" name="endDate" placeholder="<fmt:message key='plhDate'/>" class="input-xlarge" type="text">
            </div>  
            <div class="col-md-3">
                <button class="btn btn-success" type="submit" name="action" value="showMy"><fmt:message key="btnUpdateView"/></button>
            </div>  
        </div>
        <div class="container">
        <table class="table table-hover">
        <thead>
            <tr>
                <th><fmt:message key="thDate"/></th>
                <th><fmt:message key="thDuration"/></th>
                <th><fmt:message key="thCost"/></th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="callEntry" items="${callsList}">
            <tr>
                <td>${callEntry.timeStart}</td>
                <td>${callEntry.duration}</td>
                <td>${callEntry.cost}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
        </div>    
        <input type="hidden" name="section" value ="calls">
        </fieldset>
    </form>
</c:if>
    
<c:if test="${(data=='callRegisterForm')}">
    <form action="abonent" method="post">
        <fieldset>
            <div class="control-group">
                <label class="control-label" for="registerDate"><fmt:message key='lblRegisterDate'/></label>
                <div class="controls">
                    <input value="${registerDate}" name="registerDate" placeholder="<fmt:message key='plhDate'/>" class="input-xlarge" type="text">
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="duration"><fmt:message key='lblDuration'/></label>
                <div class="controls">
                    <input name="duration" placeholder="<fmt:message key='plhDuration'/>" class="input-xlarge" type="text">
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="cost"><fmt:message key='lblCost'/></label>
                <div class="controls">
                    <input name="cost" placeholder="<fmt:message key='plhCost'/>" class="input-xlarge" type="text">
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="login"></label>
                <div class="controls">
                    <button class="btn btn-success" type="submit" name="action" value="registerComplete"><fmt:message key="btnAdd"/></button>
                </div>
            </div>
            
            <input type="hidden" name="section" value ="calls">
        </fieldset>
    </form>
</c:if>