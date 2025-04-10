package com.hieu.shopBackend.utils;

public class MessageKeys {
    // login
    public static final String LOGIN_SUCCESS = "user.login.login_succesfully";
    public static final String LOGIN_FAILED = "user.login.login_failed";
    public static final String PHONE_NUMBER_EXISTED = "user.login.phone_number_existed";
    public static final String ROLE_NAME_EXISTED = "role.create.role_name_existed";
    public static final String PHONE_NUMBER_AND_PASSWORD_FAILED = "user.login.phone_number_and_password_failed";
    public static final String CAN_NOT_CREATE_ACCOUNT_ROLE_ADMIN = "user.login.can_not_account_role_admin";

    // register
    public static final String PASSWORD_NOT_MATCH = "user.register.password_not_match";
    public static final String REGISTER_SUCCESS = "user.register.register_successlly";
    public static final String USER_NOT_FOUND = "user.not_found";
    public static final String TOKEN_EXPIRATION_TIME = "token.expiration.time";
    public static final String NOT_FOUND = "not_found";
    public static final String USER_EXISTED = "user.existed";
    public static final String COMMENT_NOT_FOUND = "comment.not_found";
    public static final String UPDATE_COMMENT_SUCCESS = "comment.update.success";
    public static final String COMMENT_INSERT_SUCCESS = "comment.insert.success";

    // validation
    public static final String ERROR_MESSAGE = "message.error";
    public static final String PHONE_NUMBER_REQUIRED = "phone_number.required";
    public static final String PASSWORD_REQUIRED = "password.required";
    public static final String RETYPE_PASSWORD_REQUIRED = "retype_password.required";
    public static final String ROLE_REQUIRED = "role_name_required";
    public static final String ROLE_ID_REQUIRED = "role_id_required";
    public static final String PRODUCT_ID_REQUIRED = "product_id_required";
    public static final String IMAGE_SIZE_REQUIRED = "image_size_required";
    public static final String PRODUCT_TITLE_REQUIRED = "product_title_required";
    public static final String PRODUCT_TITLE_SIZE_REQUIRED = "product_title_size_required";
    public static final String PRODUCT_PRICE_MIN_REQUIRED = "product_price_min_required";
    public static final String PRODUCT_PRICE_MAX_REQUIRED = "product_price_max_required";
    public static final String USER_ID_REQUIRED = "user_id_required";
    public static final String PHONE_NUMBER_SIZE_REQUIRED = "phone_number_size.required";
    public static final String TOTAL_MONEY_REQUIRED = "total_money.required";
    public static final String ORDER_ID_REQUIRED = "order_id.required";
    public static final String NUMBER_OF_PRODUCT_REQUIRED = "number_of_product.required";
    public static final String CATEGORIES_NAME_REQUIRED = "categories_name.required";

    public static final String PRODUCT_NOT_FOUND = "product.valid.not_found";
    public static final String CATEGORY_NOT_FOUND = "category.valid.not_found";
    public static final String ROLE_NOT_FOUND = "role.valid.not_found";
    public static final String USER_ID_LOCKED = "user.is.lock";
    public static final String USER_ID_UNLOCKED = "user.is.unlock";
    public static final String REFRESH_TOKEN_SUCCESS = "refresh.token.success";

    // token
    public static final String ERROR_REFRESH_TOKEN = "user.refresh_token.failed";
    public static final String FILES_REQUIRED = "file.required";
    public static final String FILES_IMAGES_SUCCESS = "file.images.success";
    public static final String FILES_IMAGES_FAILED = "file.images.failed";
    public static final String FILES_IMAGES_SIZE_FAILED = "file.images.size.failed";
    public static final String FILES_IMAGES_TYPE_FAILED = "file.images.type.failed";

    // error get
    public static final String MESSAGE_ERROR_GET = "message.get.error";
    public static final String MESSAGE_UPDATE_GET = "message.update.success";
    public static final String MESSAGE_DELETE_SUCCESS = "message.delete.success";
    public static final String MESSAGE_DELETE_FAILED = "message.delete.failed";

    public static final String RESET_PASSWORD_SUCCESS = "reset.password.success";
    public static final String RESET_PASSWORD_FAILED = "reset.password.failed";

    public static final String CREATE_ROLE_SUCCESS = "role.create.successfully";
    public static final String CREATE_ORDER_SUCCESS = "order.create.successfully";
    public static final String CREATE_ORDER_FAILED = "order.create.failed";
    public static final String CREATE_ORDER_DETAILS_FAILED = "order_details.create.failed";
    public static final String CREATE_ORDER_DETAILS_SUCCESS = "order_details.create.success";
    public static final String CREATE_PRODUCT_SUCCESS = "product.create.successfully";
    public static final String CREATE_PRODUCT_FAILED = "product.create.failed";
    public static final String CREATE_CATEGORIES_SUCCESS = "category.create.successfully";
    public static final String CREATE_CATEGORIES_FAILED = "category.create.failed";
    public static final String UPDATE_CATEGORIES_SUCCESS = "category.update.successfully";
    public static final String UPDATE_CATEGORIES_FAILED = "category.update.failed";
    public static final String DELETE_CATEGORIES_SUCCESS = "category.delete.successfully";
    public static final String DELETE_CATEGORIES_FAILED = "category.delete.failed";
    public static final String DELETE_COMMENT_SUCCESS = "comment.delete.success";
    public static final String DELETE_COMMENT_FAILED = "comment.delete.failed";

    public static final String GET_INFORMATION_FAILED = "get.information.data.failed";
    public static final String GET_INFORMATION_SUCCESS = "get.information.data.success";

    public static final String APP_AUTHORIZATION_403 = "app.authorization.403";
    public static final String APP_UNCATEGORIZED_500 = "app.uncategorized.500";
    public static final String APP_PERMISSION_DENY_EXCEPTION = "app.permission.deny.exception";

}
