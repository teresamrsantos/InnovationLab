import * as actionTypes from "./actionTypes";

export const updateUserInfo = (userInfo) => ({
  type: actionTypes.UPDATE_USER_INFO,
  userInfo,
});


export const updateAuth = (authInfo) => ({
  type: actionTypes.UPDATE_AUTH,
  authInfo,

});

