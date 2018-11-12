class Answer {
    String[] answer;
    String[] f_buttons;
    String[] s_buttons;

    Answer(String[] answer, String[] f_buttons, String[] s_buttons) {
        this.answer = answer;
        this.f_buttons = f_buttons;
        this.s_buttons = s_buttons;
    }
    Answer(String[] answer, String[] f_buttons) {
        this.answer = answer;
        this.f_buttons = f_buttons;
        this.s_buttons = null;
    }
    Answer(String[] answer) {
        this.answer = answer;
        this.f_buttons = null;
        this.s_buttons = null;
    }
}
