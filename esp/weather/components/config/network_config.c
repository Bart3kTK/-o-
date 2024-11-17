#include "freertos/FreeRTOS.h"
#include "freertos/task.h"
#include "esp_system.h"
#include "esp_wifi.h"
#include "esp_event.h"
#include "nvs_flash.h"

#include "esp_log.h"

#include "network_config.h"

#define WIFI_SSID CONFIG_WIFI_SSID
#define WIFI_PASSWORD CONFIG_WIFI_PASSWORD

#define TAG "NETWORK_CONFIG"

static void event_handler(void* arg, esp_event_base_t event_base, int32_t event_id, void* event_data)
{
    if (event_base == WIFI_EVENT && event_id == WIFI_EVENT_STA_START) {
        esp_wifi_connect();
        ESP_LOGI(TAG, "Connecting to WiFi...");
    } else if (event_base == WIFI_EVENT && event_id == WIFI_EVENT_STA_DISCONNECTED) {
        ESP_LOGI(TAG, "Disconnected from WiFi, retrying...");
        esp_wifi_connect();
    } else if (event_base == WIFI_EVENT && event_id == WIFI_EVENT_STA_CONNECTED) {
        ESP_LOGI(TAG, "Connected to WiFi");
    } else if (event_base == IP_EVENT && event_id == IP_EVENT_STA_GOT_IP) {
        ip_event_got_ip_t* event = (ip_event_got_ip_t*) event_data;
        //ESP_LOGI(TAG, "Got IP: %s", esp_ip4addr_ntoa(&event->ip_info.ip));
    }
}

esp_err_t wifi_init(void)
{
    esp_err_t ret = nvs_flash_init();
    if (ret == ESP_ERR_NVS_NO_FREE_PAGES || ret == ESP_ERR_NVS_NEW_VERSION_FOUND) {
        ESP_LOGI(TAG, "Failed to initialize flash memory. Erasing NVS flash...");
        nvs_flash_erase();
        ret = nvs_flash_init();
    }

    if(ret != ESP_OK) {
        ESP_LOGE(TAG, "Failed to initialize flash memory.");
        return ret;
    }

    ret = esp_netif_init();

    if(ret != ESP_OK) {
        ESP_LOGE(TAG, "Failed to initialize network interface.");
        return ret;
    }

    ret = esp_event_loop_create_default();

    if(ret != ESP_OK) {
        ESP_LOGE(TAG, "Failed to create event loop.");
        return ret;
    }

    esp_netif_create_default_wifi_sta();

    wifi_init_config_t cfg = WIFI_INIT_CONFIG_DEFAULT();
    if(esp_wifi_init(&cfg) != ESP_OK) {
        ESP_LOGE(TAG, "Failed to initialize wifi.");
        return ESP_FAIL;
    }

    esp_event_handler_instance_t instance_any_id;
    esp_event_handler_instance_t instance_got_ip;

    if(esp_event_handler_instance_register(WIFI_EVENT, ESP_EVENT_ANY_ID, &event_handler, NULL, &instance_any_id) != ESP_OK) {
        ESP_LOGE(TAG, "Failed to register event handler for any event.");
        return ESP_FAIL;
    }

    if(esp_event_handler_instance_register(IP_EVENT, IP_EVENT_STA_GOT_IP, &event_handler, NULL, &instance_got_ip) != ESP_OK) {
        ESP_LOGE(TAG, "Failed to register event handler for IP event.");
        return ESP_FAIL;
    }

    wifi_config_t wifi_config = {
        .sta = {
            .ssid = WIFI_SSID,
            .password = WIFI_PASSWORD,
        },
    };
    
    if(esp_wifi_set_mode(WIFI_MODE_STA) != ESP_OK) {
        ESP_LOGE(TAG, "Failed to set wifi mode.");
        return ESP_FAIL;
    }

    if(esp_wifi_set_config(ESP_IF_WIFI_STA, &wifi_config) != ESP_OK) {
        ESP_LOGE(TAG, "Failed to set wifi configuration.");
        return ESP_FAIL;
    }

    if(esp_wifi_start() != ESP_OK) {
        ESP_LOGE(TAG, "Failed to start wifi.");
        return ESP_FAIL;
    }

    ESP_LOGI(TAG, "wifi_init_sta finished.");

    if(esp_wifi_connect() != ESP_OK) {
        ESP_LOGE(TAG, "Failed to connect to wifi.");
        return ESP_FAIL;
    }

    return ESP_OK;
}