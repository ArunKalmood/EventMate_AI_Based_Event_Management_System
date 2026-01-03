package com.springboard.eventmate.chat.intent;

public class ChatIntentResult {

    private ChatIntent intent;
    private String category;   // CAPS (MUSIC, TECH, etc.)
    private String keyword;

    public ChatIntentResult(ChatIntent intent) {
        this.intent = intent;
    }

    public ChatIntent getIntent() {
        return intent;
    }

    public void setIntent(ChatIntent intent) {
        this.intent = intent;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
