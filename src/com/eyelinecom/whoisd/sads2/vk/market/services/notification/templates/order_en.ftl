Order № ${order.getId()} registered!

User phone number: ${order.getUserPhoneNumber()}<br>
<br>
Order list:<br>
<br>
<#list order.getItems() as item>
${item?counter}. ${item.getName()} x ${item.getQuantity()}. Price per unit: ${item.getPrice()}<br>
</#list>
<br>
Total cost: ${order.getTotalCost()}

<#-- @ftlvariable name="order" type="com.eyelinecom.whoisd.sads2.vk.market.web.model.Order" -->