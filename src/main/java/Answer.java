class Answer {
    String[] messages;
    String[] f_buttons;
    String[] s_buttons;

    Answer(String[] messages, String[] f_buttons, String[] s_buttons) {
        this.messages = messages;
        this.f_buttons = f_buttons;
        this.s_buttons = s_buttons;
    }
    Answer(String[] messages, String[] f_buttons) {
        this.messages = messages;
        this.f_buttons = f_buttons;
        this.s_buttons = null;
    }
    Answer(String[] messages) {
        this.messages = messages;
        this.f_buttons = null;
        this.s_buttons = null;
    }
}
