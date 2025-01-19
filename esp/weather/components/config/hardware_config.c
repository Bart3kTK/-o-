#include "driver/gpio.h"
#include "driver/i2c.h"
#include "esp_err.h"
#include "esp_log.h"
#include "i2c_config.h"

#include "bh1750.h"
#include "bmp180.h"
#include "dht11.h"

#include "hardware_config.h"

i2c_dev_t bh1750;

esp_err_t hardware_init(void) {

  gpio_config_t io_conf;
  io_conf.intr_type = GPIO_INTR_DISABLE;
  io_conf.mode = GPIO_MODE_INPUT_OUTPUT;
  io_conf.pin_bit_mask =
      (1ULL << CONFIG_I2C_SDA) |
      (1ULL << CONFIG_I2C_SCL | (1ULL << GPIO_NUM_33) | (1ULL << GPIO_NUM_32));
  io_conf.pull_down_en = GPIO_PULLDOWN_DISABLE;
  io_conf.pull_up_en = GPIO_PULLUP_DISABLE;
  gpio_config(&io_conf);

  esp_err_t ret = bh1750_init_desc(&bh1750, BH1750_ADDR_LO, 0, CONFIG_I2C_SDA,
                                   CONFIG_I2C_SCL);
  if (ret != ESP_OK) {
    ESP_LOGE("HARDWARE", "BH1750 initialization failed");
  }

  ret = bh1750_power_on(&bh1750);
  if (ret != ESP_OK) {
    ESP_LOGE("HARDWARE", "BH1750 power on failed");
  }

  ret = bh1750_setup(&bh1750, BH1750_MODE_CONTINUOUS, BH1750_RES_HIGH);
  if (ret != ESP_OK) {
    ESP_LOGE("HARDWARE", "BH1750 setup failed");
  }

  ret = bh1750_set_measurement_time(&bh1750, 130);
  if (ret != ESP_OK) {
    ESP_LOGE("HARDWARE", "BH1750 measurement time setup failed");
  }

  DHT11_init(GPIO_NUM_33);

  esp_err_t err;

  err = bmp180_init(CONFIG_I2C_SDA, CONFIG_I2C_SCL);
  if (err != ESP_OK) {
    ESP_LOGE("HARDWARE", "BMP180 init failed with error = %d", err);
  }

  return ESP_OK;
}