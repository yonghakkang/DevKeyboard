package com.gazlaws.codeboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

import java.util.List;


/**
 * Created by Ruby(aka gazlaws) on 13/02/2016.
 */


public class CodeBoardIME
        extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener {
    private static final int          META_CTRL_LEFT_ON = 8192;
    private KeyboardView kv;
    private Keyboard keyboard;
    EditorInfo sEditorInfo;
    private Vibrator vibrator;
    private boolean vibratorOn;
    private boolean shiftLock;
    private boolean shift = false;
    private boolean ctrl = false;

    @Override
    public void onKey(
            int primaryCode,
            int[] KeyCodes) {

        InputConnection ic = getCurrentInputConnection();
        CharSequence txt = getCurrentInputConnection().getTextBeforeCursor(1000, 0);
        int i;
        List<Keyboard.Key> keys = keyboard.getKeys();


        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                sendDownUpKeyEvents(KeyEvent.KEYCODE_DEL);

                break;

            case 27:
                //Escape
                long now = System.currentTimeMillis();
                int meta = 0;

                ic.sendKeyEvent(new KeyEvent(now, now, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ESCAPE, 0, KeyEvent.META_CTRL_ON | KeyEvent.META_CTRL_LEFT_ON));

                break;

            case -13:
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showInputMethodPicker();

//                try {
//                    InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
//                    final IBinder token = this.getWindow().getWindow().getAttributes().token;
//                    //imm.setInputMethod(token, LATIN);
//                    imm.switchToLastInputMethod(token);
//                } catch (Throwable t) { // java.lang.NoSuchMethodError if API_level<11
//                    Log.e("WHAA","cannot set the previous input method:");
//                    t.printStackTrace();
//                }
                break;

            case 18:
                ctrl = !ctrl;

                for(i=0; i < keys.size();i++) {
                    if (ctrl) {
                        if (keys.get(i).label.equals("Ctrl")) {
                            keys.get(i).label = "CTRL";
                            break;
                        }
                    } else {
                        if (keys.get(i).label.equals("CTRL")) {
                            keys.get(i).label = "Ctrl";
                            break;
                        }
                    }
                }
                kv.invalidateAllKeys();
                break;

            case 53737:
                getCurrentInputConnection().performContextMenuAction(android.R.id.selectAll);
                break;
            case 53738:
                getCurrentInputConnection().performContextMenuAction(android.R.id.cut);
                break;
            case 53739:
                getCurrentInputConnection().performContextMenuAction(android.R.id.copy);
                break;
            case 53740:
                getCurrentInputConnection().performContextMenuAction(android.R.id.paste);
                break;

            case Keyboard.KEYCODE_SHIFT:

                shift = !shift;
                keyboard.setShifted(shift);

                if (shift){
                    for(i=0; i < keys.size();i++){
                        if(keys.get(i).label.equals("Shift")) {
                            keys.get(i).label = "SHIFT";
                            break;
                        }
                    }
                } else for(i=0; i < keys.size();i++){
                    //Log.e("3","shift");
                    if(keys.get(i).label.equals("SHIFT")) {

                        keys.get(i).label = "Shift";
                        break;
                    }
                }
                kv.invalidateAllKeys();
                break;

            case Keyboard.KEYCODE_DONE:
                //sendDownUpKeyEvents(KeyEvent.KEYCODE_ENTER);
                switch (sEditorInfo.imeOptions & (EditorInfo.IME_MASK_ACTION | EditorInfo.IME_FLAG_NO_ENTER_ACTION)) {
                    case EditorInfo.IME_ACTION_GO:
                        ic.performEditorAction(EditorInfo.IME_ACTION_GO);
                        break;
                    case EditorInfo.IME_ACTION_NEXT:
                        ic.performEditorAction(EditorInfo.IME_ACTION_NEXT);
                        break;
                    case EditorInfo.IME_ACTION_DONE:
                        ic.performEditorAction(EditorInfo.IME_ACTION_DONE);
                        break;
                    case EditorInfo.IME_ACTION_SEARCH:
                        ic.performEditorAction(EditorInfo.IME_ACTION_SEARCH);
                        break;

                    case EditorInfo.IME_ACTION_SEND:
                        ic.performEditorAction(EditorInfo.IME_ACTION_SEND);
                        break;

                    default:
                        ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                        break;
                }
                break;


            case 9:
                //tab
                // ic.commitText("\u0009", 1);
                sendDownUpKeyEvents(KeyEvent.KEYCODE_TAB);
                break;

            case 14:
                sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_LEFT);
                break;
            case 15:
                sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_UP);
                break;
            case 16:
                sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_DOWN);
                break;
            case 17:
                sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_RIGHT);
                break;

            default:
                char code = (char) primaryCode;
                if (ctrl) {
                    switch (code) {
                        case 'a':
                        case 'A':
                            getCurrentInputConnection().performContextMenuAction(android.R.id.selectAll);
                            ctrl = false;
                            for(i=0; i < keys.size();i++){
                                if(keys.get(i).label.equals("CTRL")) {
                                    keys.get(i).label = "Ctrl";
                                    break;
                                }
                            }
                            kv.invalidateKey(i);
                            break;
                        case 'c':
                        case 'C':
                            getCurrentInputConnection().performContextMenuAction(android.R.id.copy);
                            ctrl = false;
                            for(i=0; i < keys.size();i++){
                                if(keys.get(i).label.equals("CTRL")) {
                                    keys.get(i).label = "Ctrl";
                                    break;
                                }
                            }
                            kv.invalidateKey(i);
                            break;
                        case 'v':
                        case 'V':
                            getCurrentInputConnection().performContextMenuAction(android.R.id.paste);
                            ctrl = false;
                            for(i=0; i < keys.size();i++){
                                if(keys.get(i).label.equals("CTRL")) {
                                    keys.get(i).label = "Ctrl";
                                    break;
                                }
                            }
                            kv.invalidateKey(i);
                            break;
                        case 'x':
                        case 'X':
                            getCurrentInputConnection().performContextMenuAction(android.R.id.cut);
                            ctrl = false;
                            for(i=0; i < keys.size();i++){
                                if(keys.get(i).label.equals("CTRL")) {
                                    keys.get(i).label = "Ctrl";
                                    break;
                                }
                            }
                            kv.invalidateKey(i);
                            break;
                        case 'z':
//                            event = new KeyEvent(event.getDownTime(), event.getEventTime(),
//                            event.getAction(), event.getKeyCode(), event
//                                    .getRepeatCount(), event.getDeviceId(), event
//                                    .getScanCode(), KeyEvent.META_SHIFT_LEFT_ON
//                                    | KeyEvent.META_SHIFT_ON);
                            if (shift) {
                                long now2 = System.currentTimeMillis();
                                ic.sendKeyEvent(new KeyEvent(now2, now2, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_Z, 0, KeyEvent.META_CTRL_ON | KeyEvent.META_CTRL_LEFT_ON | KeyEvent.META_SHIFT_ON | KeyEvent.META_SHIFT_LEFT_ON));

                                shift = false;
                                    for(i=0; i < keys.size(); i++){
                                        if(keys.get(i).label.equals("SHIFT")) {

                                            keys.get(i).label = "Shift";
                                            break;
                                        }
                                    }
                                keyboard.setShifted(shift);
                                kv.invalidateAllKeys();

                            } else {
                                long now2 = System.currentTimeMillis();
                                ic.sendKeyEvent(new KeyEvent(now2, now2, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_Z, 0, KeyEvent.META_CTRL_ON | KeyEvent.META_CTRL_LEFT_ON));
                            }

                            ctrl = false;
                            for(i=0; i < keys.size();i++){
                                if(keys.get(i).label.equals("CTRL")) {
                                    keys.get(i).label = "Ctrl";
                                    break;
                                }
                            }
                            kv.invalidateAllKeys();
                            break;

                        default:
                            ic.commitText(String.valueOf(code), 1);
                            ctrl = false;
                            for(i=0; i < keys.size();i++){
                                if(keys.get(i).label.equals("CTRL")) {
                                    keys.get(i).label = "Ctrl";
                                    break;
                                }
                            }
                            kv.invalidateAllKeys();
                            break;

                    }

                    if(shift){
                        shift=false;
                        for(i=0; i < keys.size();i++){
                            if(keys.get(i).label.equals("SHIFT")) {
                                keys.get(i).label = "Shift";
                                break;
                            }
                        }
                        keyboard.setShifted(shift);
                        kv.invalidateAllKeys();
                    }
                } else if (Character.isLetter(code) && shift) {
                    code = Character.toUpperCase(code);
                    ic.commitText(String.valueOf(code), 1);
                    if(!shiftLock) {
                        shift = false;

                        for (i = 0; i < keys.size(); i++) {
                            //Log.e("5", "shift");
                            if (keys.get(i).label.equals("SHIFT")) {

                                keys.get(i).label = "Shift";
                                break;
                            }
                        }
                        keyboard.setShifted(shift);
                        kv.invalidateAllKeys();
                    }


                } else ic.commitText(String.valueOf(code), 1);

        }

    }


    @Override
    public void onPress(int primaryCode) {
        if (vibratorOn) {
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(20);
        }
    }


    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onText(CharSequence text) {
    }

    @Override
    public void swipeDown() {

        kv.closing();

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeUp() {

    }

    @Override
    public View onCreateInputView() {

        SharedPreferences pre = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);

        switch (pre.getInt("RADIO_INDEX_COLOUR", 0)) {
            case 0:
                //kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
                kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
                break;
            case 1:
                kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard1, null);
                break;
            case 2:
                kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard2, null);
                break;
            case 3:
                kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard3, null);
                break;
            case 4:
                kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard4, null);
                break;
            case 5:
                kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard5, null);
                break;

            default:
                kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);

                break;


        }

        if (pre.getInt("PREVIEW", 0) == 1) {
            kv.setPreviewEnabled(true);
        } else kv.setPreviewEnabled(false);

        if (pre.getInt("VIBRATE", 1) == 1) {
            vibratorOn = true;
        } else vibratorOn = false;

       if (pre.getInt("RADIO_INDEX_LAYOUT", 0) == 0) {

           if(pre.getInt("ARROW_ROW",1) == 1){

               if (pre.getInt("SIZE", 2) == 0) {
                 //  keyboard = new Keyboard(this, R.xml.qwerty_arrow_small);
               }
               //else if (pre.getInt("SIZE", 2) == 1) {
                  // keyboard = new Keyboard(this, R.xml.qwerty_arrow);}
               else if (pre.getInt("SIZE", 2) == 3) {
                  // keyboard = new Keyboard(this, R.xml.qwerty_large_arrow);
               } //else keyboard = new Keyboard(this, R.xml.qwerty_medium_arrow);
           }
           else {

               if (pre.getInt("SIZE", 2) == 0) {
                  // keyboard = new Keyboard(this, R.xml.qwerty_small);
               } else if (pre.getInt("SIZE", 2) == 1) {
                   keyboard = new Keyboard(this, R.xml.qwerty);
               }else if (pre.getInt("SIZE", 2) == 3) {
                   keyboard = new Keyboard(this, R.xml.qwerty_large);
               } else keyboard = new Keyboard(this, R.xml.qwerty_medium);
           }
        }
       else {
           if(pre.getInt("ARROW_ROW",1) == 1) {
               if (pre.getInt("SIZE", 2) == 0) {
                  // keyboard = new Keyboard(this, R.xml.azerty_arrow_small);
               } else if (pre.getInt("SIZE", 2) == 1) {
                   //keyboard = new Keyboard(this, R.xml.azerty_arrow);
               } else if (pre.getInt("SIZE", 2) == 3) {
                   //keyboard = new Keyboard(this, R.xml.azerty_large_arrow);
               } //else keyboard = new Keyboard(this, R.xml.azerty_medium_arrow);
           }
           else {
               if (pre.getInt("SIZE", 2) == 0) {
                  // keyboard = new Keyboard(this, R.xml.azerty_small);
               } else if (pre.getInt("SIZE", 2) == 1) {
                  // keyboard = new Keyboard(this, R.xml.azerty);
               } else if (pre.getInt("SIZE", 2) == 3) {
                  // keyboard = new Keyboard(this, R.xml.azerty_large);
               } //else keyboard = new Keyboard(this, R.xml.azerty_medium);
           }
        }

        if(pre.getInt("SHIFT",0)==1){
            shiftLock=true;
        } else shiftLock=false;

        shift=false;
        ctrl=false;
//do this for symbols
        if (keyboard !=null){

            kv.setKeyboard(keyboard);
            kv.setOnKeyboardActionListener(this);

        }

        return kv;
    }

    @Override
    public void onStartInputView(EditorInfo attribute, boolean restarting) {
        super.onStartInputView(attribute, restarting);

        setInputView(onCreateInputView());
        sEditorInfo = attribute;

    }
}


