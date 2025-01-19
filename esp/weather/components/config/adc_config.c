#include "esp_adc/adc_cali.h"
#include "esp_adc/adc_cali_scheme.h"
#include "esp_adc/adc_oneshot.h"
#include "soc/adc_channel.h"

#include "adc_config.h"

#include "esp_log.h"

#define CHANNEL_NUM 2

static adc_channel_t channels[CHANNEL_NUM] = {ADC_CHANNEL_2, ADC_CHANNEL_3};
adc_oneshot_unit_handle_t adc_handle;

esp_err_t adc_init(void) {

  adc_oneshot_unit_init_cfg_t init_config = {
      .unit_id = ADC_UNIT_1,
  };

  esp_err_t err = adc_oneshot_new_unit(&init_config, &adc_handle);
  if (err != ESP_OK) {
    ESP_LOGE("ADC", "Failed to initialize ADC unit 1");
    return err;
  }

  adc_oneshot_chan_cfg_t config = {
      .atten = ADC_ATTEN_DB_11,
      .bitwidth = ADC_BITWIDTH_DEFAULT,
  };

  for (int i = 0; i < CHANNEL_NUM; i++) {
    err = adc_oneshot_config_channel(adc_handle, channels[i], &config);
    if (err != ESP_OK) {
      ESP_LOGE("ADC", "Failed to initialize channel %d", i);
      return err;
    }
  }

  return ESP_OK;
}

esp_err_t adc_read(uint8_t channel, float *adc_reading) {
  int raw_reading;
  esp_err_t err = adc_oneshot_read(adc_handle, channel, &raw_reading);
  if (err != ESP_OK) {
    ESP_LOGE("ADC", "Failed to read from channel %d", channel);
  }

  *adc_reading = ((float)raw_reading * 1.66) / 4095.0;

  return ESP_OK;
}

esp_err_t adc_read_mh_rd(float *adc_reading) {
  return adc_read(channels[0], adc_reading);
}

esp_err_t adc_read_mq135(float *adc_reading) {
  return adc_read(channels[1], adc_reading);
}
