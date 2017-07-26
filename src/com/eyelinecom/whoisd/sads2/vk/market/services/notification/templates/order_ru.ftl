Заказ № ${order.getId()} зарегистрирован!

Номер телефона пользователя: ${order.getUserPhoneNumber()}<br>
<br>
Список товаров:<br>
<br>
<#list order.getItems() as item>
${item?counter}. ${item.getName()} x ${item.getQuantity()}. Цена за ед.: ${item.getPrice()}<br>
</#list>
<br>
Итого к оплате: ${order.getTotalCost()}

<#-- @ftlvariable name="order" type="com.eyelinecom.whoisd.sads2.vk.market.web.model.Order" -->