package org.telegram.messenger.openAI;
import android.content.SharedPreferences;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Settings specific to OpenAI service.
 */
public class OpenAISettings extends BaseServiceSettings {

    // Field keys
    private static final String KEY_API_KEY = "api_key";
    private static final String KEY_MODEL = "model";
    private static final String KEY_TEMPERATURE = "temperature";
    private static final String KEY_MAX_TOKENS = "max_tokens";
    private static final String KEY_TOP_P = "top_p";
    private static final String KEY_FREQUENCY_PENALTY = "frequency_penalty";
    private static final String KEY_PRESENCE_PENALTY = "presence_penalty";

    // Default values
    public static final String DEFAULT_MODEL = "gpt-3.5-turbo";
    public static final float DEFAULT_TEMPERATURE = 0.9f;
    public static final int DEFAULT_MAX_TOKENS = 1500;
    public static final float DEFAULT_TOP_P = 0.95f;
    public static final float DEFAULT_FREQUENCY_PENALTY = 0.5f;
    public static final float DEFAULT_PRESENCE_PENALTY = 0.6f;

    // Fields
    private String apiKey = "";
    private String model = DEFAULT_MODEL;
    private float temperature = DEFAULT_TEMPERATURE;
    private int maxTokens = DEFAULT_MAX_TOKENS;
    private float topP = DEFAULT_TOP_P;
    private float frequencyPenalty = DEFAULT_FREQUENCY_PENALTY;
    private float presencePenalty = DEFAULT_PRESENCE_PENALTY;

    public OpenAISettings(int account) {
        super(account);
        load();
    }

    @Override
    public void load() {
        apiKey = getString(KEY_API_KEY, "");
        model = getString(KEY_MODEL, DEFAULT_MODEL);
        temperature = getFloat(KEY_TEMPERATURE, DEFAULT_TEMPERATURE);
        maxTokens = getInt(KEY_MAX_TOKENS, DEFAULT_MAX_TOKENS);
        topP = getFloat(KEY_TOP_P, DEFAULT_TOP_P);
        frequencyPenalty = getFloat(KEY_FREQUENCY_PENALTY, DEFAULT_FREQUENCY_PENALTY);
        presencePenalty = getFloat(KEY_PRESENCE_PENALTY, DEFAULT_PRESENCE_PENALTY);
        org.telegram.messenger.FileLog.d("OpenAISettings load: temperature=" + temperature + ", topP=" + topP + ", frequencyPenalty=" + frequencyPenalty + ", presencePenalty=" + presencePenalty + ", maxTokens=" + maxTokens + ", model=" + model);
        // Debug logging of keys
        org.telegram.messenger.FileLog.d("OpenAISettings load keys: " +
                "apiKeyKey=" + getKey(KEY_API_KEY) +
                ", modelKey=" + getKey(KEY_MODEL) +
                ", temperatureKey=" + getKey(KEY_TEMPERATURE) +
                ", maxTokensKey=" + getKey(KEY_MAX_TOKENS) +
                ", topPKey=" + getKey(KEY_TOP_P) +
                ", frequencyPenaltyKey=" + getKey(KEY_FREQUENCY_PENALTY) +
                ", presencePenaltyKey=" + getKey(KEY_PRESENCE_PENALTY));
    }

    @Override
    public void save() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getKey(KEY_API_KEY), apiKey);
        editor.putString(getKey(KEY_MODEL), model);
        editor.putFloat(getKey(KEY_TEMPERATURE), temperature);
        editor.putInt(getKey(KEY_MAX_TOKENS), maxTokens);
        editor.putFloat(getKey(KEY_TOP_P), topP);
        editor.putFloat(getKey(KEY_FREQUENCY_PENALTY), frequencyPenalty);
        editor.putFloat(getKey(KEY_PRESENCE_PENALTY), presencePenalty);
        try {
            editor.apply();
            org.telegram.messenger.FileLog.d("OpenAISettings: Saved all preferences atomically");
        } catch (Exception e) {
            org.telegram.messenger.FileLog.e("OpenAISettings: Failed to save preferences", e);
        }
    }

    @Override
    public boolean validate() {
        return !TextUtils.isEmpty(apiKey);
    }

    @Override
    public AISettings.AIServiceType getServiceType() {
        return AISettings.AIServiceType.OPENAI;
    }

    @Override
    public String getDefaultModelId() {
        return DEFAULT_MODEL;
    }

    @Override
    public String getApiKey() {
        return apiKey;
    }

    @Override
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey != null ? apiKey : "";
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public void setModel(String model) {
        this.model = model != null ? model : DEFAULT_MODEL;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public int getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(int maxTokens) {
        this.maxTokens = maxTokens;
    }

    public float getTopP() {
        return topP;
    }

    public void setTopP(float topP) {
        this.topP = topP;
    }

    public float getFrequencyPenalty() {
        return frequencyPenalty;
    }

    public void setFrequencyPenalty(float frequencyPenalty) {
        this.frequencyPenalty = frequencyPenalty;
    }

    public float getPresencePenalty() {
        return presencePenalty;
    }

    public void setPresencePenalty(float presencePenalty) {
        this.presencePenalty = presencePenalty;
    }

    @Override
    public void setValue(String key, Object value) {
        org.telegram.messenger.FileLog.d("OpenAISettings setValue: key=" + key + " value=" + value + " (type=" + (value != null ? value.getClass().getSimpleName() : "null") + ")");
        switch (key) {
            case KEY_TEMPERATURE:
                if (value instanceof Number) {
                    float temp = ((Number) value).floatValue();
                    org.telegram.messenger.FileLog.d("OpenAISettings setting temperature = " + temp);
                    setTemperature(temp);
                }
                break;
            case KEY_MAX_TOKENS:
                if (value instanceof Number) {
                    int tokens = ((Number) value).intValue();
                    org.telegram.messenger.FileLog.d("OpenAISettings setting maxTokens = " + tokens);
                    setMaxTokens(tokens);
                }
                break;
            case KEY_TOP_P:
                if (value instanceof Number) {
                    float topP = ((Number) value).floatValue();
                    org.telegram.messenger.FileLog.d("OpenAISettings setting topP = " + topP);
                    setTopP(topP);
                }
                break;
            case KEY_FREQUENCY_PENALTY:
                if (value instanceof Number) {
                    float freq = ((Number) value).floatValue();
                    org.telegram.messenger.FileLog.d("OpenAISettings setting frequencyPenalty = " + freq);
                    setFrequencyPenalty(freq);
                }
                break;
            case KEY_PRESENCE_PENALTY:
                if (value instanceof Number) {
                    float pres = ((Number) value).floatValue();
                    org.telegram.messenger.FileLog.d("OpenAISettings setting presencePenalty = " + pres);
                    setPresencePenalty(pres);
                }
                break;
            case KEY_API_KEY:
                if (value instanceof String) {
                    org.telegram.messenger.FileLog.d("OpenAISettings setting API key (masked)");
                    setApiKey((String) value);
                }
                break;
            case KEY_MODEL:
                if (value instanceof String) {
                    org.telegram.messenger.FileLog.d("OpenAISettings setting model = " + value);
                    setModel((String) value);
                }
                break;
        }
        super.setValue(key, value);
    }

    @Override
    public String getServiceDisplayName() {
        return "OpenAI";
    }

    @Override
    public List<SettingDefinition> getSettingDefinitions() {
        List<SettingDefinition> definitions = new ArrayList<>();
        definitions.add(new SettingDefinition.Builder()
                .setKey(KEY_API_KEY)
                .setType(SettingType.STRING)
                .setTitle("API Key")
                .setDescription("Ключ API от OpenAI. Начинается с 'sk-'.")
                .setMasked(true)
                .setDefaultValue("")
                .build());
        definitions.add(new SettingDefinition.Builder()
                .setKey(KEY_MODEL)
                .setType(SettingType.CHOICE)
                .setTitle("Модель")
                .setDescription("Модель OpenAI для генерации текста.")
                .setMasked(false)
                .setDefaultValue(DEFAULT_MODEL)
                .setConstraints(new HashMap<String, Object>() {{
                    put("choices", Arrays.asList(
                            "gpt-3.5-turbo",
                            "gpt-4",
                            "gpt-4-turbo",
                            "gpt-4o",
                            "gpt-4o-mini",
                            "gpt-4o-2024-08-06"
                    ));
                }})
                .build());
        definitions.add(new SettingDefinition.Builder()
                .setKey(KEY_TEMPERATURE)
                .setType(SettingType.FLOAT)
                .setTitle("Температура")
                .setDescription("Контроль случайности ответов (0.0 - 2.0).")
                .setMasked(false)
                .setDefaultValue(DEFAULT_TEMPERATURE)
                .setConstraints(new HashMap<String, Object>() {{
                    put("min", 0.0f);
                    put("max", 2.0f);
                }})
                .build());
        definitions.add(new SettingDefinition.Builder()
                .setKey(KEY_MAX_TOKENS)
                .setType(SettingType.INT)
                .setTitle("Максимальное количество токенов")
                .setDescription("Ограничение длины ответа в токенах.")
                .setMasked(false)
                .setDefaultValue(DEFAULT_MAX_TOKENS)
                .setConstraints(new HashMap<String, Object>() {{
                    put("min", 1);
                    put("max", 100000);
                }})
                .build());
        definitions.add(new SettingDefinition.Builder()
                .setKey(KEY_TOP_P)
                .setType(SettingType.FLOAT)
                .setTitle("Top P")
                .setDescription("Альтернатива температуре, ядерная выборка (0.0 - 1.0).")
                .setMasked(false)
                .setDefaultValue(DEFAULT_TOP_P)
                .setConstraints(new HashMap<String, Object>() {{
                    put("min", 0.0f);
                    put("max", 1.0f);
                }})
                .build());
        definitions.add(new SettingDefinition.Builder()
                .setKey(KEY_FREQUENCY_PENALTY)
                .setType(SettingType.FLOAT)
                .setTitle("Frequency Penalty")
                .setDescription("Штраф за частоту слов (-2.0 - 2.0).")
                .setMasked(false)
                .setDefaultValue(DEFAULT_FREQUENCY_PENALTY)
                .setConstraints(new HashMap<String, Object>() {{
                    put("min", -2.0f);
                    put("max", 2.0f);
                }})
                .build());
        definitions.add(new SettingDefinition.Builder()
                .setKey(KEY_PRESENCE_PENALTY)
                .setType(SettingType.FLOAT)
                .setTitle("Presence Penalty")
                .setDescription("Штраф за присутствие слов (-2.0 - 2.0).")
                .setMasked(false)
                .setDefaultValue(DEFAULT_PRESENCE_PENALTY)
                .setConstraints(new HashMap<String, Object>() {{
                    put("min", -2.0f);
                    put("max", 2.0f);
                }})
                .build());
        return definitions;
    }
}