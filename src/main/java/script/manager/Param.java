/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package script.manager;

/**
 *
 * @author ryoji
 */
public enum Param {
    GOAL
}
enum Goal {
    INIT_BASHSM_DB,
    SET_SCRIPT_HOME,
    SET_EXEC_TEMPLATE,
    SET_FILE_EXTENSION,
    REGISTER,
    PRINT,
    EXECUTE,
    NONE
}
enum SetFileExtensionOption {
    FILE_EXTENSION
}
enum SetExecTemplateOption {
    EXEC_TEMPLATE
}
enum SetScriptHomeOption {
    ABSOLUTE_PATH
}
enum RegisterOption {
    ALL_AS_RAN,
    OVERRIDE,
    RAN
}
enum PrintOption {
    NOT_RAN,
    DIFFER,
    SCRIPT_NO,
    SCRIPT_CONTENTS
}