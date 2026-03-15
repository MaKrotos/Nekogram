package org.telegram.messenger.openAI;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.UserConfig;

import java.util.List;
import java.util.Map;

/**
 * Base class for service-specific settings.
 * Each AI service should extend this class and define its own fields.
 */
public abstract class BaseServiceSettings {

    protected int account;
    protected SharedPreferences preferences;
    protected String preferencePrefix;

    public BaseServiceSettings(int account) {
        this.account = account;
        this.preferences = ApplicationLoader.applicationContext.getSharedPreferences(
                getSharedPreferencesName(), Context.MODE_PRIVATE);
        this.preferencePrefix = getPreferencePrefix();
    }

    /**
     * Load settings from SharedPreferences into this object's fields.
     */
    public abstract void load();

    /**
     * Save current fields to SharedPreferences.
     */
    public abstract void save();

    /**
     * Validate the settings (e.g., API key not empty).
     * @return true if settings are valid for making requests.
     */
    public abstract boolean validate();

    /**
     * Get the service type this settings belong to.
     */
    public abstract AISettings.AIServiceType getServiceType();

    /**
     * Get the default model ID for this service.
     */
    public abstract String getDefaultModelId();

    /**
     * Get the API key for this service.
     */
    public abstract String getApiKey();

    /**
     * Set the API key for this service.
     */
    public abstract void setApiKey(String apiKey);

    /**
     * Get the model ID for this service.
     */
    public abstract String getModel();

    /**
     * Set the model ID for this service.
     */
    public abstract void setModel(String model);

    /**
     * Get the display name of the service.
     */
    public abstract String getServiceDisplayName();

    /**
     * Get the list of setting definitions for this service.
     * Each definition describes a configurable parameter (API key, temperature, etc.)
     */
    public abstract List<SettingDefinition> getSettingDefinitions();

    /**
     * Get the current value of a setting by its key.
     * @param key the setting key (without prefix)
     * @return the value as Object (String, Integer, Float, Boolean) or null if not found.
     */
    public Object getValue(String key) {
        SettingDefinition def = findDefinition(key);
        if (def == null) {
            return null;
        }
        switch (def.getType()) {
            case STRING:
                return getString(key, def.getStringDefault());
            case INT:
                return getInt(key, def.getIntDefault());
            case FLOAT:
                return getFloat(key, def.getFloatDefault());
            case BOOLEAN:
                return getBoolean(key, def.getBooleanDefault());
            case CHOICE:
                return getString(key, def.getStringDefault());
            default:
                return null;
        }
    }

    /**
     * Set the value of a setting by its key.
     * @param key the setting key (without prefix)
     * @param value the value (must match the setting type)
     */
    public void setValue(String key, Object value) {
        SettingDefinition def = findDefinition(key);
        if (def == null) {
            return;
        }
        switch (def.getType()) {
            case STRING:
            case CHOICE:
                if (value instanceof String) {
                    putString(key, (String) value);
                }
                break;
            case INT:
                if (value instanceof Integer) {
                    putInt(key, (Integer) value);
                } else if (value instanceof Number) {
                    putInt(key, ((Number) value).intValue());
                }
                break;
            case FLOAT:
                if (value instanceof Float) {
                    putFloat(key, (Float) value);
                } else if (value instanceof Number) {
                    putFloat(key, ((Number) value).floatValue());
                }
                break;
            case BOOLEAN:
                if (value instanceof Boolean) {
                    putBoolean(key, (Boolean) value);
                }
                break;
        }
    }

    private SettingDefinition findDefinition(String key) {
        for (SettingDefinition def : getSettingDefinitions()) {
            if (def.getKey().equals(key)) {
                return def;
            }
        }
        return null;
    }

    // Helper methods

    protected String getSharedPreferencesName() {
        return "ai_service_settings_" + account;
    }

    protected String getPreferencePrefix() {
        return getServiceType().name().toLowerCase() + "_";
    }

    protected String getKey(String suffix) {
        return preferencePrefix + suffix;
    }

    protected void putString(String key, String value) {
        try {
            preferences.edit().putString(getKey(key), value).apply();
            FileLog.d("BaseServiceSettings: Saved string preference: " + key + " = " + (key.toLowerCase().contains("key") ? "***" : value));
        } catch (Exception e) {
            FileLog.e("BaseServiceSettings: Failed to save string preference: " + key, e);
        }
    }

    protected String getString(String key, String defaultValue) {
        try {
            return preferences.getString(getKey(key), defaultValue);
        } catch (ClassCastException e) {
            FileLog.e("BaseServiceSettings: Invalid type for string preference: " + key, e);
            return defaultValue;
        }
    }

    protected void putInt(String key, int value) {
        try {
            preferences.edit().putInt(getKey(key), value).apply();
            FileLog.d("BaseServiceSettings: Saved int preference: " + key + " = " + value);
        } catch (Exception e) {
            FileLog.e("BaseServiceSettings: Failed to save int preference: " + key, e);
        }
    }

    protected int getInt(String key, int defaultValue) {
        try {
            int value = preferences.getInt(getKey(key), defaultValue);
            FileLog.d("BaseServiceSettings: Loaded int preference: " + key + " = " + value + " (fullKey=" + getKey(key) + ")");
            return value;
        } catch (ClassCastException e) {
            FileLog.e("BaseServiceSettings: Invalid type for int preference: " + key, e);
            return defaultValue;
        }
    }

    protected void putFloat(String key, float value) {
        try {
            preferences.edit().putFloat(getKey(key), value).apply();
            FileLog.d("BaseServiceSettings: Saved float preference: " + key + " = " + value);
        } catch (Exception e) {
            FileLog.e("BaseServiceSettings: Failed to save float preference: " + key, e);
        }
    }

    protected float getFloat(String key, float defaultValue) {
        try {
            float value = preferences.getFloat(getKey(key), defaultValue);
            FileLog.d("BaseServiceSettings: Loaded float preference: " + key + " = " + value + " (fullKey=" + getKey(key) + ")");
            return value;
        } catch (ClassCastException e) {
            FileLog.e("BaseServiceSettings: Invalid type for float preference: " + key, e);
            return defaultValue;
        }
    }

    protected void putBoolean(String key, boolean value) {
        try {
            preferences.edit().putBoolean(getKey(key), value).apply();
            FileLog.d("BaseServiceSettings: Saved boolean preference: " + key + " = " + value);
        } catch (Exception e) {
            FileLog.e("BaseServiceSettings: Failed to save boolean preference: " + key, e);
        }
    }

    protected boolean getBoolean(String key, boolean defaultValue) {
        try {
            return preferences.getBoolean(getKey(key), defaultValue);
        } catch (ClassCastException e) {
            FileLog.e("BaseServiceSettings: Invalid type for boolean preference: " + key, e);
            return defaultValue;
        }
    }

    /**
     * Clear all settings for this service.
     */
    public void clear() {
        SharedPreferences.Editor editor = preferences.edit();
        for (String key : preferences.getAll().keySet()) {
            if (key.startsWith(preferencePrefix)) {
                editor.remove(key);
            }
        }
        try {
            editor.apply();
        } catch (Exception e) {
            FileLog.e("BaseServiceSettings: Failed to clear preferences", e);
        }
    }
}