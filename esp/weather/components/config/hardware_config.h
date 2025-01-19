#ifndef HARDWARE_CONFIG_H
#define HARDWARE_CONFIG_H

#include "esp_err.h"
#include "i2c_config.h"

extern i2c_dev_t bh1750;

esp_err_t hardware_init(void);

#endif // HARDWARE_CONFIG_H