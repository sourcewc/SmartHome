package com.appbaragi.smarthome;

/**
 * Created by kim on 2017-02-04.
 */

import cloud.artik.api.DeviceTypesApi;
import cloud.artik.client.ApiClient;
import cloud.artik.client.ApiException;
import cloud.artik.client.Configuration;
import cloud.artik.client.auth.OAuth;
import cloud.artik.model.ManifestVersionsEnvelope;



public class DeviceTypesApiExample {

    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();

        // Configure OAuth2 access token for authorization: artikcloud_oauth
        OAuth artikcloud_oauth = (OAuth) defaultClient.getAuthentication("artikcloud_oauth");
        artikcloud_oauth.setAccessToken("YOUR ACCESS TOKEN");

        DeviceTypesApi apiInstance = new DeviceTypesApi();
        String deviceTypeId = "deviceTypeId_example"; // String | deviceTypeId
        try {
            ManifestVersionsEnvelope result = apiInstance.getAvailableManifestVersions(deviceTypeId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DeviceTypesApi#getAvailableManifestVersions");
            e.printStackTrace();
        }
    }
}