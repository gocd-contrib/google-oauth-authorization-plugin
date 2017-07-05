# Google oauth plugin for GoCD

## Requirements

* GoCD server version v17.5.0 or above
* Google [API credentials](https://console.developers.google.com/apis/credentials)

## Installation

Copy the file `build/libs/google-oauth-authorization-plugin-VERSION.jar` to the GoCD server under `${GO_SERVER_DIR}/plugins/external` 
and restart the server. The `GO_SERVER_DIR` is usually `/var/lib/go-server` on Linux and `C:\Program Files\Go Server` 
on Windows.

## Configuration

###  Configure Google API credentials

1. Login to [API credentials](https://console.developers.google.com/apis/credentials)
2. Click on **_Create credentials_** and select `OAuth Client Id`
4. Select `Web application` as a Application type 
5. Provide appropriate **_Name_** to your api credentials
6. Specify `http://<<your.go.server.hostname.or.ip>>/go/plugin/cd.go.authorization.google/authenticate` in **_Authorized redirect URIs
_**. Click on **_Create_**
7. Yay!! You have created google api credentials
8. Note **_Client ID_** and **_Client Secret_**, you will need it to configure plugin in next step

### Configure Plugin

1. Login to `GoCD server` as admin and navigate to **_Admin_** _>_ **_Security_** _>_ **_Authorization Configuration_**
2. Click on **_Add_** to create new authorization configuration
    1. Specify `id` for auth config
    2. Select `Google oauth authorization plugin for GoCD` for **_Plugin id_**
    3. Specify **_Client ID_** and **_Client Secret_**
    4. Optionally, you can specify `Allowed Domains` settings to restrict user login from specified domains
    5. Save your configuration