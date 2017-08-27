/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.input;

public interface InputListener {

    /**
     * Handle input event received from fragment
     *
     * @param cs view id
     */
    void onInputReceived(CharSequence cs);

    /**
     * Handle 'calculate result' callback
     */
    void onCalculateResult();

    /**
     * Handle 'clear input' callback
     */
    void onClearInput();

    /**
     * Handle 'delete input' callback
     */
    void onDeleteInput();

    /**
     * Handle 'insert brackets' callback
     */
    void onInsertBrackets();

    /**
     * Handle 'memory store' callback
     */
    void onMemoryStore();

    /**
     * Handle 'memory recall' callback
     */
    void onMemoryRecall();

    /**
     * Handle 'insert answer' callback
     */
    void onInsertAnswer();
}
