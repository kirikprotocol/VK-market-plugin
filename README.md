# VK Market

## Goal

Making of purchases at VK Market, including user's connection to the market, picking up goods, purchase completion.

## Usage

The plugin is called using a link of the type: ```http://plugins.miniapps.run/vk-market?....``` 

For example:
```
<page version="2.0">
  <navigation>
    <link pageId="http://plugins.miniapps.run/vk-market?vk_access_token=<some_access_token>&vk_user_id=<some_user_id>&merchant_email=bob@example.com&locale=ru&exit_url=<some_exit_url>">
       Go to VK Market
    </link>
  </navigation>
</page>
```

## Parameters
|Parameters		|Mandatory	|Description								|
|:----------------------|:-------------:|:----------------------------------------------------------------------|
|vk_access_token	|Yes		|Access token to VK Market API.						|
|vk_user_id		|Yes		|User ID in VK (for the seller).					|
|merchant_email		|Yes		|Seller's email address (for notifications).				|
|exit_url		|Yes		|The URL to go to after exiting the plugin.				|
|locale			|No		|Text language. Supported languages: RU, EN - default.			|
