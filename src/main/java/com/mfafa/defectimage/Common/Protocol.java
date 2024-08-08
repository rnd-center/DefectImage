package com.mfafa.defectimage.Common;

public class Protocol {

    public static final String KEY_HASH                     = "___MFAFA_TEXT12345_!@#$%_ABCDEF_*&^%";

    public static class Response {
        public static final String Code = "_code";
        public static final String Result = "_result";
        public static final String Message = "_message";
        public static final String Value = "_value";

        public static final String NeedUpdate = "_needUpdate";
        public static final String Version = "_version";
        public static final String ReleaseDate = "_releaseDate";
        public static final String Changes = "_changes";
        public static final String DownloadUrl = "_downloadUrl";
    }

    public static class Files{
        public static final String KEY_PATTERN_FILE				= "_Pattern";
        public static final String KEY_PIECE_FILE				= "_PieceFile";
        public static final String KEY_PIECE_IMAGE				= "_PieceImage";
        public static final String KEY_SKETCH_FILE				= "_Sketch";
        public static final String KEY_TNP_FILE					= "_TnP";
    }

    public static class StateCode {

        public static final int KEY_SUCCESS = 200;
        // Page Error
        public static final int ERR_PAGE_NOT_FOUND = 404;
        // Class Error : 600
        public static final int ERR_CLASS_NOT_ENOUGH_RESULT = 600;
        public static final int ERR_SELECT_CLASS = 601;
        // Auth Error
        public static final int ERR_IS_NOT_MEMBER = 700;
        public static final int ERR_LOGIN_FAILED = 701;
    }

    public static class Status {
        public static final String ALERT                = "__ALERT";
        public static final String WARNING              = "__WARNING";
        public static final String DANGER               = "__DANGER";
        public static final String ERROR                = "__ERROR";
        public static final String INFO                 = "__INFO";
        public static final String DEBUG                = "__DEBUG";

        public static final String YES                  = "Y";
        public static final String NO                   = "N";
        public static final String DELETE               = "D";
        public static final String HIDE                 = "H";
        public static final String USED                 = "U";
        public static final String ALL                  = "A";
        public static final String QUESTION             = "Q";
        public static final String RESPONSE             = "R";

        public static final int KEY_NONE                = -1;
        public static final int KEY_YES                 = 1;
        public static final int KEY_NO                  = 0;

    }
}