#include "driver/gpio.h"
#include "driver/i2c.h"
#include "esp_err.h"
#include "esp_log.h"
#include "i2c_config.h"

#include "bh1750.h"

#include "hardware_config.h"

esp_err_t hardware_init(void) {

  i2c_dev_t bh1750;
  esp_err_t ret = bh1750_init_desc(&bh1750, BH1750_ADDR_LO, 0, CONFIG_I2C_SDA,
                                   CONFIG_I2C_SCL);
  if (ret != ESP_OK) {
    ESP_LOGE("HARDWARE", "BH1750 initialization failed");
    return ret;
  }

  ret = bh1750_power_on(&bh1750);
  if (ret != ESP_OK) {
    ESP_LOGE("HARDWARE", "BH1750 power on failed");
    return ret;
  }

  ret = bh1750_setup(&bh1750, BH1750_MODE_CONTINUOUS, BH1750_RES_HIGH);
  if (ret != ESP_OK) {
    ESP_LOGE("HARDWARE", "BH1750 setup failed");
    return ret;
  }

  if (ret != ESP_OK) {
    ESP_LOGE("HARDWARE", "I2C setup failed");
    return ret;
  }

  ret = bh1750_init_desc(&bh1750, BH1750_ADDR_LO, 0, CONFIG_I2C_SDA,
                         CONFIG_I2C_SCL);
  if (ret != ESP_OK) {
    ESP_LOGE("HARDWARE", "BH1750 initialization failed");
    return ret;
  }

  uint16_t level = 2137;
  ret = bh1750_read(&bh1750, &level);

  if (ret != ESP_OK) {
    ESP_LOGE("HARDWARE", "BH1750 read failed");
    return ret;
  }

  ESP_LOGI("HARDWARE", "BH1750 read: %d", level);

  return ESP_OK;
}