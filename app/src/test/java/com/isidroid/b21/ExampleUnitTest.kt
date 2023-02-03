package com.isidroid.b21

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test",  which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val data = A.values()
        val bgColors = data.map { it.bgColor }.distinct()
        val headerColor = data.map { it.headerColor }.distinct()

        println(bgColors)
        println(headerColor)
    }
}

enum class A(val bgColor: String, val headerColor: String) {
    NEW(
        bgColor = "R.color.audit_new",
        headerColor = "R.color.audit_new_header",
    ),

    NEW_CHECKING(
        bgColor = "R.color.audit_new",
        headerColor = "R.color.audit_new_header",
    ),

    IN_PROGRESS(
        bgColor = "R.color.audit_progress",
        headerColor = "R.color.audit_progress_header",
    ),

    ASSIGNED(
        bgColor = "R.color.audit_progress",
        headerColor = "R.color.audit_progress_header",
    ),

    ON_CONTROL(
        bgColor = "R.color.audit_progress",
        headerColor = "R.color.audit_progress_header",
    ),
    ON_REWORK(
        bgColor = "R.color.audit_progress",
        headerColor = "R.color.audit_progress_header",
    ),

    CHECK(
        bgColor = "R.color.audit_check",
        headerColor = "R.color.audit_check_header",
    ),
    ON_CHECK(
        bgColor = "R.color.audit_on_check",
        headerColor = "R.color.audit_on_check_header",
    ),

    MODIFY(
        bgColor = "R.color.audit_modify",
        headerColor = "R.color.audit_modify_header",
    ),

    ANSWERS_MODIFIED_SENDING(
        bgColor = "R.color.audit_modify",
        headerColor = "R.color.audit_modify_header",
    ),

    ON_MODIFY(
        bgColor = "R.color.audit_modify",
        headerColor = "R.color.audit_modify_header",
    ),

    ACCEPTED(
        bgColor = "R.color.audit_accepted",
        headerColor = "R.color.audit_accepted_header",
    ),

    FAILED(
        bgColor = "R.color.bg_course_card_failed_v2",
        headerColor = "R.color.ic_course_state_failed",
    ),

    COMPLETED(
        bgColor = "R.color.audit_completed",
        headerColor = "R.color.audit_completed_header",
    ),

    ANSWERS_SENDING(
        bgColor = "R.color.audit_progress",
        headerColor = "R.color.audit_progress_header",
    ),

    CHECK_SENDING(
        bgColor = "R.color.audit_new",
        headerColor = "R.color.audit_new_header",
    ),

    UNASSIGNED(
        bgColor = "R.color.audit_accepted",
        headerColor = "R.color.list_state_nav_icon_text_color_unchecked",
    ),

    UNKNOWN(
        bgColor = "R.color.audit_new",
        headerColor = "R.color.audit_new_header",
    );
}