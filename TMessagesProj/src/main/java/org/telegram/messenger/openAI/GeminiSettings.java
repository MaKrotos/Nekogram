package org.telegram.messenger.openAI;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Settings specific to Google Gemini service.
 */
public class GeminiSettings extends BaseServiceSettings {

    // Field keys
    private static final String KEY_API_KEY = "api_key";
    private static final String KEY_MODEL = "model";
    private static final String KEY_TEMPERATURE = "temperature";
    private static final String KEY_MAX_OUTPUT_TOKENS = "max_output_tokens";
    private static final String KEY_TOP_P = "top_p";
    private static final String KEY_TOP_K = "top_k";
    private static final String KEY_SAFETY_SETTINGS = "safety_settings";

    // Default values
    public static final String DEFAULT_MODEL = "gemini-pro";
    public static final float DEFAULT_TEMPERATURE = 0.9f;
    public static final int DEFAULT_MAX_OUTPUT_TOKENS = 1500;
    public static final float DEFAULT_TOP_P = 0.95f;
    public static final int DEFAULT_TOP_K = 40;
    public static final String DEFAULT_SAFETY_SETTING = "BLOCK_NONE";

    // Fields
    private String apiKey = "";
    private String model = DEFAULT_MODEL;
    private float temperature = DEFAULT_TEMPERATURE;
    private int maxOutputTokens = DEFAULT_MAX_OUTPUT_TOKENS;
    private float topP = DEFAULT_TOP_P;
    private int topK = DEFAULT_TOP_K;
    private Map<String, String> safetySettings = new HashMap<>();

    public GeminiSettings(int account) {
        super(account);
        load();
    }

    @Override
    public void load() {
        apiKey = getString(KEY_API_KEY, "");
        model = getString(KEY_MODEL, DEFAULT_MODEL);
        temperature = getFloat(KEY_TEMPERATURE, DEFAULT_TEMPERATURE);
        maxOutputTokens = getInt(KEY_MAX_OUTPUT_TOKENS, DEFAULT_MAX_OUTPUT_TOKENS);
        topP = getFloat(KEY_TOP_P, DEFAULT_TOP_P);
        topK = getInt(KEY_TOP_K, DEFAULT_TOP_K);
        loadSafetySettings();
    }

    private void loadSafetySettings() {
        safetySettings.clear();
        String json = getString(KEY_SAFETY_SETTINGS, "");
        if (!TextUtils.isEmpty(json)) {
            try {
                JSONArray array = new JSONArray(json);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    String category = obj.optString("category");
                    String threshold = obj.optString("threshold");
                    if (category != null && threshold != null) {
                        safetySettings.put(category, threshold);
                    }
                }
            } catch (Exception e) {
                // ignore
            }
        }
        // If empty, set defaults
        if (safetySettings.isEmpty()) {
            safetySettings.put("HARM_CATEGORY_HARASSMENT", DEFAULT_SAFETY_SETTING);
            safetySettings.put("HARM_CATEGORY_HATE_SPEECH", DEFAULT_SAFETY_SETTING);
            safetySettings.put("HARM_CATEGORY_SEXUALLY_EXPLICIT", DEFAULT_SAFETY_SETTING);
            safetySettings.put("HARM_CATEGORY_DANGEROUS_CONTENT", DEFAULT_SAFETY_SETTING);
        }
    }

    @Override
    public void save() {
        putString(KEY_API_KEY, apiKey);
        putString(KEY_MODEL, model);
        putFloat(KEY_TEMPERATURE, temperature);
        putInt(KEY_MAX_OUTPUT_TOKENS, maxOutputTokens);
        putFloat(KEY_TOP_P, topP);
        putInt(KEY_TOP_K, topK);
        saveSafetySettings();
    }

    private void saveSafetySettings() {
        try {
            JSONArray array = new JSONArray();
            for (Map.Entry<String, String> entry : safetySettings.entrySet()) {
                JSONObject obj = new JSONObject();
                obj.put("category", entry.getKey());
                obj.put("threshold", entry.getValue());
                array.put(obj);
            }
            putString(KEY_SAFETY_SETTINGS, array.toString());
        } catch (Exception e) {
            // ignore
        }
    }

    @Override
    public boolean validate() {
        return !TextUtils.isEmpty(apiKey);
    }

    @Override
    public AISettings.AIServiceType getServiceType() {
        return AISettings.AIServiceType.GEMINI;
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

    public int getMaxOutputTokens() {
        return maxOutputTokens;
    }

    public void setMaxOutputTokens(int maxOutputTokens) {
        this.maxOutputTokens = maxOutputTokens;
    }

    public float getTopP() {
        return topP;
    }

    public void setTopP(float topP) {
        this.topP = topP;
    }

    public int getTopK() {
        return topK;
    }

    public void setTopK(int topK) {
        this.topK = topK;
    }

    public Map<String, String> getSafetySettings() {
        return safetySettings;
    }

    public void setSafetySettings(Map<String, String> safetySettings) {
        this.safetySettings = safetySettings != null ? safetySettings : new HashMap<>();
    }

    public void setSafetySetting(String category, String threshold) {
        safetySettings.put(category, threshold);
    }

    public String getSafetySetting(String category) {
        return safetySettings.get(category);
    }

    @Override
    public void setValue(String key, Object value) {
        switch (key) {
            case KEY_TEMPERATURE:
                if (value instanceof Number) {
                    setTemperature(((Number) value).floatValue());
                }
                break;
            case KEY_MAX_OUTPUT_TOKENS:
                if (value instanceof Number) {
                    setMaxOutputTokens(((Number) value).intValue());
                }
                break;
            case KEY_TOP_P:
                if (value instanceof Number) {
                    setTopP(((Number) value).floatValue());
                }
                break;
            case KEY_TOP_K:
                if (value instanceof Number) {
                    setTopK(((Number) value).intValue());
                }
                break;
            case KEY_API_KEY:
                if (value instanceof String) {
                    setApiKey((String) value);
                }
                break;
            case KEY_MODEL:
                if (value instanceof String) {
                    setModel((String) value);
                }
                break;
            case KEY_SAFETY_SETTINGS:
                if (value instanceof String) {
                    // Обработка JSON строки безопасности
                    // Пока просто сохраняем строку, но нужно также обновить safetySettings
                    // Для простоты игнорируем, так как это сложный объект
                }
                break;
        }
        super.setValue(key, value);
    }

    @Override
    public String getServiceDisplayName() {
        return "Google Gemini";
    }
    @Override
    public List<SettingDefinition> getSettingDefinitions() {
        List<SettingDefinition> definitions = new ArrayList<>();
        definitions.add(new SettingDefinition.Builder()
                .setKey(KEY_API_KEY)
                .setType(SettingType.STRING)
                .setTitle("API Key")
                .setDescription("Ключ API от Google AI Studio. Начинается с 'AIza'.")
                .setMasked(true)
                .setDefaultValue("")
                .build());
        definitions.add(new SettingDefinition.Builder()
                .setKey(KEY_MODEL)
                .setType(SettingType.CHOICE)
                .setTitle("Модель")
                .setDescription("Модель Gemini для генерации текста.")
                .setMasked(false)
                .setDefaultValue(DEFAULT_MODEL)
                .setConstraints(new HashMap<String, Object>() {{
                    put("choices", Arrays.asList(
                            "gemini-pro",
                            "gemini-pro-vision",
                            "gemini-1.5-pro",
                            "gemini-1.5-flash",
                            "gemini-1.5-pro-latest"
                    ));
                }})
                .build());
        definitions.add(new SettingDefinition.Builder()
                .setKey(KEY_TEMPERATURE)
                .setType(SettingType.FLOAT)
                .setTitle("Температура")
                .setDescription("Контроль случайности ответов (0.0 - 1.0).")
                .setMasked(false)
                .setDefaultValue(DEFAULT_TEMPERATURE)
                .setConstraints(new HashMap<String, Object>() {{
                    put("min", 0.0f);
                    put("max", 1.0f);
                }})
                .build());
        definitions.add(new SettingDefinition.Builder()
                .setKey(KEY_MAX_OUTPUT_TOKENS)
                .setType(SettingType.INT)
                .setTitle("Максимальное количество токенов")
                .setDescription("Ограничение длины ответа в токенах.")
                .setMasked(false)
                .setDefaultValue(DEFAULT_MAX_OUTPUT_TOKENS)
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
                .setKey(KEY_TOP_K)
                .setType(SettingType.INT)
                .setTitle("Top K")
                .setDescription("Количество наиболее вероятных токенов для выборки (1-40).")
                .setMasked(false)
                .setDefaultValue(DEFAULT_TOP_K)
                .setConstraints(new HashMap<String, Object>() {{
                    put("min", 1);
                    put("max", 40);
                }})
                .build());
        // Safety settings - сложный объект, пока оставим как строку JSON с пояснением
        definitions.add(new SettingDefinition.Builder()
                .setKey(KEY_SAFETY_SETTINGS)
                .setType(SettingType.STRING)
                .setTitle("Настройки безопасности")
                .setDescription("JSON-массив настроек безопасности (категория и порог).")
                .setMasked(false)
                .setDefaultValue("")
                .build());
        return definitions;
    }
}