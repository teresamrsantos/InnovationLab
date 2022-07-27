var generalPath = 'http://localhost:8080/Backend/rest/'

/*********************Method of the Controler USER **********************/

/************************************************************************/
/******************************* LOGIN API ******************************/
/************************************************************************/

export function loginAPI(emailInput, passwordInput, onSuccess, onError) {
  var myJSON = JSON.stringify({
    email: emailInput,
    password: passwordInput,
  });

  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: myJSON,
  };

  fetch(generalPath + "users/login", fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/********************************************************/
/********************* Register API *********************/
/********************************************************/

export function registerAPI(myJSON, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    body: myJSON,
  };

  fetch(generalPath + "users/register", fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response;
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/**************************************************************/
/********************* recoverPasswordAPI *********************/
/**************************************************************/

export function recoverPasswordAPI(email, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json" },
  };

  fetch(generalPath + "users/forgotMyPassword/" + email, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.text();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error);
    });
}

/*****************************************************************************/
/********************************* resetPasswordAPI **************************/
/*****************************************************************************/

export function resetPasswordAPI(resetToken, newPassword, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", resetToken: resetToken, },
    body: newPassword,
  };

  fetch(generalPath + "users/resetPassword", fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.text();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error);
    });
}


/**********************************************************/
/******************** getUserInfo - API *******************/
/**********************************************************/

export function getUserInfoAPI(token, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token: token, },
  };

  fetch(generalPath + "users", fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error);
    });
}


/*******************************************************************/
/********************** getUserInterests - API *********************/
/*******************************************************************/

export function getUserInterestsAPI(id, token, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token: token, },
  };

  fetch(generalPath + "users/interestsUser", fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error);
    });
}

/****************************************************************/
/*********************** getUserSkills - API ********************/
/****************************************************************/

export function getUserSkillsAPI(id, token, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token: token, },
  };

  fetch(generalPath + "users/skillsUser", fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error);
    });
}

/************************************************************************/
/********************** change User Password - API **********************/
/************************************************************************/

export function changeUserPasswordAPI(token, myJson, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token: token, },
    body: myJson,
  };

  fetch(generalPath + "users/editPassword", fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.text();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error);
    });
}

/**********************************************************************/
/********************* edit User Profile - API ************************/
/**********************************************************************/

export function editUserProfileAPI(token, myJson, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { token: token, },
    body: myJson,
  };

  fetch(generalPath + "users/editProfile", fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.text();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error);
    });
}

/*************************************************************************************/
/***************************** associate Interest to user - API **********************/
/*************************************************************************************/

export function associateInterestToUserAPI(token, id, idInterest, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token: token, },
  };

  fetch(generalPath + "users/associateInterest?idInterest=" + idInterest, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error);
    });
}

/****************************************************************************************/
/**************************** disassociate Interest to user - API ***********************/
/****************************************************************************************/

export function disassociateInterestToUserAPI(token, id, idInterest, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token: token, },
  };

  fetch(generalPath + "users/disassociateInterest?idInterest=" + idInterest, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error);
    });
}

/*******************************************************************************/
/*********************** associate Skill to user - API *************************/
/*******************************************************************************/

export function associatSkillToUserAPI(token, id, idSkill, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token: token, },
  };

  fetch(generalPath + "users/associateSkill?idSkill=" + idSkill, fetchProperties)

    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {

      onSuccess(json);
    })
    .catch(function (error) {

      onError(error);
    });
}

/**********************************************************************************/
/************************** disassociate Skill to user - API **********************/
/**********************************************************************************/

export function disassociateSkillToUserAPI(token, id, idSkill, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token: token, },
  };

  fetch(generalPath + "users/disassociateSkill?idSkill=" + idSkill, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error);
    });
}

/*******************************************************************************/
/****************************Change visibility User  ***************************/
/*******************************************************************************/

export function changeVisibilityUser(token, id, visibility, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token },
    body: '"' + visibility + '"'
  };

  fetch(generalPath + "users/editProfileVisibility/" + id, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.text();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error)
    });
}

/*************************************************************************************/
/************************* addUserSpecificVisibility *********************************/
/*************************************************************************************/

export function addUserSpecificVisibility(token, idToAdd, userID, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token },
  };
  fetch(generalPath + "users/SpecificVisibility/AddMember/" + userID + '?idsToAdd=' + idToAdd, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.text();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error)
    });
}

/********************************************************************************************/
/********************************** remove UserSpecificVisibility ***************************/
/********************************************************************************************/

export function removeUserSpecificVisibility(token, idsToRemove, userID, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token },
  };
  fetch(generalPath + "users/SpecificVisibility/RemoveMember/" + userID + '?idsToRemove=' + idsToRemove, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.text();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error)
    });
}

/*******************************************************************************/
/***************** getAllUsersWithAvailableIdeaNecessity - API *****************/
/*******************************************************************************/

export function getAllIdeaNecessityUserIsCreatorAPI( token, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token },
  };

  fetch(generalPath + "users/ideaNecessity/author", fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error);
    });
}

/****************************************************************/
/*************** getAllFavoriteIdeaNecessity - API **************/
/****************************************************************/

export function getAllFavoriteIdeaNecessityAPI(token, onSuccess) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token }
  };

  fetch(generalPath + "users/ideaNecessity/favorite", fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      console.log(error)
    });
}

/****************************************************************************************/
/********************* findAllUsersNotParticipatingInProject - API **********************/
/****************************************************************************************/

export function findAllUsersNotParticipatingInProject_API(token, id, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token },
  };


  fetch(generalPath + "users/usersNotParticipating/" + id, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error);
    });
}

/******************************************************************/
/********************** getUserInfoById - API *********************/
/******************************************************************/

export function getUserInfoByIdAPI(id, token, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token: token, },
  };

  fetch(generalPath + "users/find/" + id, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error);
    });
}

/******************************************************************************/
/****************** getAllProjectUserIsAuthorByPermission - API ***************/
/******************************************************************************/

export function getAllProjectUserIsAuthorByPermissionAPI(id, token, onSuccess) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token: token, },
  };

  fetch(generalPath + "users/project/author/" + id, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

 console.log(error);
    });
}

/*************************************************************************/
/********************** Change UserType - API ****************************/
/*************************************************************************/

export function changeUseTypeAPI(token, id, usertype, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token: token, },
    body: '"' + usertype + '"'
  };

  fetch(generalPath + "users/usertype/" + id, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error);
    });
}

/********************************************/
/*****************Logout- API ***************/
/********************************************/

export function logoutAPI(token, onSuccess) {
  var fetchProperties = {
    method: "POST",
    headers: { token: token },
  };

  fetch(generalPath + "users/logout", fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.text();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      console.log(error)
    });
}

/*************************************************************/
/******************** getUserById - API **********************/
/*************************************************************/

export function getUserByIdAPI(id, token, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token: token, },
  };

  fetch(generalPath + "users/profile/" + id, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error);
    });
}

/*****************************************************/
/*******************Activation- API ******************/
/*****************************************************/

export function activationAPI(activation, onSuccess) {
  var fetchProperties = {
    method: "POST",
    headers: { activation: activation },
  };

  fetch(generalPath + "users/activation", fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.text();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      console.log(error)
    });
}

/**********Method of the Controler Notification ***************/

/**************************************************************/
/************* getNumberfUnreadNotifications - API ************/
/**************************************************************/

export function countUnreadNotificationsAPI(token, onSuccess) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token: token, },
  };

  fetch(generalPath + "notifController/countNotificationsToRead", fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

    });
}

/***********************************************************************/
/********************* get All Notifications - API *********************/
/***********************************************************************/

export function getAllallUserNotificationsAPI(idUser, token, onSuccess) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token },
  };


  fetch(generalPath + "notifController/allUserNotifications/" + idUser, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      console.log(error)
    });
}


/********************************************************************/
/******************** markNotificationasRead - API ******************/
/********************************************************************/

export function markNotificationasReadAPI(idNotif, token, onSuccess) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token },
  };

  fetch(generalPath + "notifController/notificationRead/" + idNotif, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.text();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      console.log(error)
    });
}

/****************************************************************/
/****************** deleteNotification - API ********************/
/****************************************************************/

export function deleteNotificationAPI(idNotif, token, onSuccess) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token },
  };


  fetch(generalPath + "notifController/deleteNotification/" + idNotif, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.text();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      console.log(error)
    });
}

/**********Method of the Controler Message **************/

/********************************************************/
/************* getNumberfUnreadMessages- API ************/
/********************************************************/

export function countUnreadMessagesAPI(token, onSuccess) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token: token, },
  };

  fetch(generalPath + "message/countUnreadMessages", fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

    });
}

/************************************************************************/
/******************** Info User Conversation - API **********************/
/************************************************************************/

export function getInfoUserConversationAPI(id,token, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token: token, },
  };

  fetch(generalPath + "message/userConversation/"+id, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error);
    });
}


/************************************************************************/
/****************** getAllUserWithConversation - API ********************/
/************************************************************************/

export function getAllUserWithConversationAPI(token, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token },
  };

  fetch(generalPath + "message/conversation", fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
   .catch(function (error) {
     onError(error);
   });
}

/***************************************************************/
/********************* getAllUsers - API ***********************/
/***************************************************************/

export function getAllUsersToTalkAPI(token, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token },
  };

  fetch(generalPath + "message/users", fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
   .catch(function (error) {
     onError(error);
   });
}

/***************************************************************/
/********************* getAllUsers Except Himself - API **********/
/***************************************************************/

export function findAllUserExceptHimselfAPI(token, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token },
  };


  fetch(generalPath + "users/exceptHimself/get", fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
   .catch(function (error) {
     onError(error);
   });
}


/********************************************************************************************/
/***************************** get Conversation Between Users - API *************************/
/********************************************************************************************/

export function getConversationBetweenUsersAPI(token, emailConversation, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token },
  };

  fetch(generalPath + "message/conversation/" + emailConversation, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
    onError(error);
    });
}

/******************************************************************/
/********************* Send Message - API *************************/
/******************************************************************/

export function sendMessageAPI(myJSON, token, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token },
    body: myJSON,
  };

  fetch(generalPath + "message/send", fetchProperties)
    .then(function (response) {

      if (response.ok) {
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .catch(function (error) {
    onError(error);
  });
}

/****************************************************************/
/********************* Read Message - API ***********************/
/****************************************************************/

export function readMessageAPI(email, token, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token }
  };

  fetch(generalPath + "message/read/" + email, fetchProperties)
    .then(function (response) {
      if (response.ok) {
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .catch(function (error) {
    onError(error);
  });
}

/************Method of the Controler IdeaNecessity ***************/

/*****************************************************************/
/******************* getAllIdeaNecessity - API *******************/
/*****************************************************************/

export function getAllIdeaNecessityAPI(token, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token },
  };

  fetch(generalPath + "ideaNecessity/allIdeaNecessity", fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error);
    });
}

/*****************************************************************/
/******************* getAllIdeaNecessity - API *******************/
/*****************************************************************/

export function getAllIdeaNecessityNotDeletedAPI(token, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token },
  };

  fetch(generalPath + "ideaNecessity/allIdeaNecessityNotDeleted", fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error);
    });
}

/************************************************************************/
/********************** Favorite IdeaNecessity - API ********************/
/************************************************************************/

export function favoriteIdeaNecessityAPI(id, token, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token },
  };

  fetch(generalPath + "ideaNecessity/favorite/" + id, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.text();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);

    });
}

/*****************************************************************************/
/********************* Remove Favorite IdeaNecessity - API *******************/
/*****************************************************************************/

export function removeFavoriteIdeaNecessityAPI(id, token, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token },
  };

  fetch(generalPath + "ideaNecessity/removeFavorite/" + id, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.text();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/***************************************************************************/
/********************* Add Idea Necessity - API ****************************/
/***************************************************************************/

export function addIdeaNecessityAPI(myJSON, token, id, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { token: token },
    body: myJSON,
  };

  fetch(generalPath + "ideaNecessity/addIdeaNecessity/" + id, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.text();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/*********************************************************************************/
/************************* Associate IdeaNecessity - API *************************/
/*********************************************************************************/

export function associateIdeaNecessityAPI(id, myJSON, token, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token },
    body: myJSON,
  };

  fetch(generalPath + "ideaNecessity/associateIdeaNecessity/" + id, fetchProperties)
    .then(function (response) {
      if (response.ok) {
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/*************************************************************************/
/******************* getAllIdeaNecessityNoDelete - API *******************/
/*************************************************************************/

export function getAllIdeaNecessityNoDeleteAPI(token, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token },
  };

  fetch(generalPath + "ideaNecessity/allIdeaNecessityNoDelete", fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error);
    });
}

/********************************************************************/
/******************* getAllIdeaNecessitySELECT - API ****************/
/********************************************************************/

export function getAllIdeaNecessitySELECT(token, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token },
  };

  fetch(generalPath + "ideaNecessity/allIdeaNecessityselect", fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error);
    });
}

/*************************************************************************/
/********************* getAllIdeaNecessityById - API *********************/
/*************************************************************************/

export function getAllIdeaNecessityByIdAPI(id, token, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token },
  };

  fetch(generalPath + "ideaNecessity/" + id, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error);
    });
}

/*******************************************************************/
/********************* Vote IdeaNecessity - API ********************/
/*******************************************************************/

export function voteIdeaNecessityAPI(id, token, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token },
  };

  fetch(generalPath + "ideaNecessity/vote/" + id, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.text();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

    });
}

/*************************************************************************/
/******************** Remove Vote IdeaNecessity - API ********************/
/*************************************************************************/
export function removeVoteIdeaNecessityAPI(id, token, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token },
  };

  fetch(generalPath + "ideaNecessity/removeVote/" + id, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.text();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
    });
}

/***************************************************************************/
/********************** availability IdeaNecessity - API *******************/
/***************************************************************************/

export function availabilityIdeaNecessityAPI(id, token, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token },
  };

  fetch(generalPath + "ideaNecessity/availability/" + id, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.text();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

    });
}

/*********************************************************************************/
/******************** Remove Availability IdeaNecessity - API ********************/
/*********************************************************************************/
export function removeAvailabilityIdeaNecessityAPI(id, token, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token },
  };

  fetch(generalPath + "ideaNecessity/removeAvailability/" + id, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.text();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
    });
}

/***************************************************************************************/
/********************* getAllUsersWithAvailableIdeaNecessity - API *********************/
/***************************************************************************************/

export function getAllUsersWithAvailableIdeaNecessityAPI(id, token, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token },
  };

  fetch(generalPath + "ideaNecessity/available/" + id, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error);
    });
}

/**********************************************************************************/
/********************** Desassociate IdeaNecessity - API **************************/
/**********************************************************************************/

export function desassociateIdeaNecessityAPI(id, idAss, token, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token },
  };

  fetch(generalPath + "ideaNecessity/disassociateIdeaNecessity/" + id + "/" + idAss, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.text();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/*********************************************************************************/
/*********************** getAllIdsIdeaNecessityAssociate - API *******************/
/*********************************************************************************/

export function getAllIdsIdeaNecessityAssociateAPI(id, token, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token },
  };

  fetch(generalPath + "ideaNecessity/idIdeaNecessityAssociate/" + id, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/***********************************************************************************/
/******************** getAllSkillAssociateIdeaNecessity - API **********************/
/***********************************************************************************/

export function getAllSkillAssociateIdeaNecessityAPI(id, token, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token },
  };

  fetch(generalPath + "ideaNecessity/skill/" + id, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/**************************************************************************************/
/*********************** getAllInterestAssociateIdeaNecessity - API *******************/
/**************************************************************************************/

export function getAllInterestAssociateIdeaNecessityAPI(id, token, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token },
  };

  fetch(generalPath + "ideaNecessity/interest/" + id, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/*********************************************************************************************************/
/******************************** associate Interest the a IdeaNecessity - API ***************************/
/*********************************************************************************************************/

export function associateInterestIdeaNecessityAPI(token, idIdeaNecessity, idInterest, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token: token, },
  };

  fetch(generalPath + "ideaNecessity/associateInterest/" + idIdeaNecessity + "/" + idInterest, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error);
    });
}

/************************************************************************************************************/
/*********************************** disassociate Interest the a IdeaNecessity - API ************************/
/************************************************************************************************************/

export function disassociateInterestIdeaNecessityAPI(token, idIdeaNecessity, idInterest, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token: token, },
  };

  fetch(generalPath + "ideaNecessity/disassociateInterest/" + idIdeaNecessity + "/" + idInterest, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error);
    });
}

/***************************************************************************************************/
/***************************** associate Skill the a IdeaNecessity - API ***************************/
/***************************************************************************************************/

export function associateSkillIdeaNecessityAPI(token, idIdeaNecessity, idSkill, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token: token, },
  };

  fetch(generalPath + "ideaNecessity/associateSkill/" + idIdeaNecessity + "/" + idSkill, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error);
    });
}

/******************************************************************************************************/
/**************************** disassociate Skill the a IdeaNecessity - API ****************************/
/******************************************************************************************************/

export function disassociateSkillIdeaNecessityAPI(token, idIdeaNecessity, idSkill, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token: token, },
  };

  fetch(generalPath + "ideaNecessity/disassociateSkill/" + idIdeaNecessity + "/" + idSkill, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/****************************************************************************/
/********************** Edit Idea Necessity - API ***************************/
/****************************************************************************/

export function editIdeaNecessityAPI(myJSON, id, token, onSuccess, onError) {

  var fetchProperties = {
    method: "POST",
    headers: { token: token },
    body: myJSON,
  };

  fetch(generalPath + "ideaNecessity/editIdeaNecessity/" + id, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.text();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/*********************************************************************/
/****************** Delete Idea Necessity - API **********************/
/*********************************************************************/

export function deleteIdeaNecessityAPI(id, token, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { token: token },
  };

  fetch(generalPath + "ideaNecessity/softDelete/" + id, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.text();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/**************** Method of the Controler Interest ****************/

/******************************************************************/
/********************** searchInterest - API **********************/
/******************************************************************/

export function searchInterestAPI(token, word, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token },
  };

  fetch(generalPath + "interest/activeSearch/" + word, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error);
    });
}

/**********************************************************************/
/*********************** add Interest API *****************************/
/**********************************************************************/

export function addInterestAPI(token, description, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token: token, },
    body: JSON.stringify({ description: description })
  };

  fetch(generalPath + "interest/addInterest", fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error);
    });
}

/******************************************************************/
/*************** get all Interest Select - API ********************/
/******************************************************************/

export function getAllInterestSelectAPI(token, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token },
  };

  fetch(generalPath + "interest/allInterestSelect", fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
  /*  .catch(function (error) {
      onError(error);
    });*/
}

/**************************************************************/
/********************** GET Interest BY ID ********************/
/**************************************************************/
export function getInterestById(id, token, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token },
  };

  fetch(generalPath + "interest/" + id, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error);
    });
}

/**************** Method of the Controler Skill ****************/

/***************************************************************/
/*********************** searchSkill - API *********************/
/***************************************************************/

export function searchSkillAPI(token, word, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token },
  };

  fetch(generalPath + "skill/activeSearch/" + word, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error);
    });
}
/**************************************************************/
/************************ add Skill  API **********************/
/**************************************************************/

export function addSkillAPI(token, myjson, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token: token, },
    body: myjson
  };

  fetch(generalPath + "skill/addSkill", fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/***************************************************************/
/*************** get all Skill Object Select- API **************/
/***************************************************************/

export function getAllSkillSelectAPI(token, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token },
  };

  fetch(generalPath + "skill/allSkillSelect", fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
  /*.catch(function (error) {
    onError(error);
  });*/
}

/***********************************************************/
/********************** GET SKILL BY ID ********************/
/***********************************************************/
export function getskillById(id, token, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token },
  };

  fetch(generalPath + "skill/" + id, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error);
    });
}

/********************* Method of the Controler Project ************************/

/******************************************************************************/
/******************************** AddProject - API ****************************/
/******************************************************************************/

export function addProjectAPI(myJSON, token, idPIdeas, id, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { token: token },
    body: myJSON,
  };

  fetch(generalPath + "projectController/addProject/" + id + '?idProjectIdeas=' + idPIdeas, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.text();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/************************************************************/
/****************** get All Projects - API ******************/
/************************************************************/
export function getAllProjectsAPI(token, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token: token, },
  };

  fetch(generalPath + "projectController/allProjects", fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/***************************************************************************/
/*********************** get a specific Projects - API *********************/
/***************************************************************************/
export function getSpecificProjectAPI(token, idProject, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token: token, },
  };

  fetch(generalPath + "projectController/" + idProject, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/*****************************************************************************/
/********************* get all members of Projects - API *********************/
/*****************************************************************************/
export function findAllMembersOfProject(token, idProject, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token: token, },
  };

 
  fetch(generalPath + "participationController/usersMember/" + idProject, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/*****************************************************************************/
/********************* Ask to be a member of project - API *******************/
/*****************************************************************************/

export function requestToJoinProjectAPI(token, idProject, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token: token },
  };

  fetch(generalPath + "projectController/" + idProject + "/requestToJoinProject", fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.text();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/*****************************************************************************/
/*************************** AddProjectFavorites - API ***********************/
/*****************************************************************************/

export function addProjectFavoritesAPI(token, idProject, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token: token },
  };

  fetch(generalPath + "projectController/addFavorite/" + idProject, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.text();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/*******************************************************************************/
/************************** RemoveProjectFavorites - API ***********************/
/*******************************************************************************/

export function removeProjectFavoritesAPI(token, idProject, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token: token },
  };

  fetch(generalPath + "projectController/removeFavorite/" + idProject, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.text();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/***************************************************************************************************/
/******************************** Change role member Project - API *********************************/
/***************************************************************************************************/

export function changeMemberTypeProject_API(token, idProject, participationDTO, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token: token },
    body: participationDTO
  };

  fetch(generalPath + "projectController/" + idProject + "/changeMemberType", fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/*************************************************************************************/
/**************************** Change role member Project - API ***********************/
/*************************************************************************************/

export function removeMemberProject_API(token, idProject, idUser, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token: token },

  };

  fetch(generalPath + "projectController/" + idProject + "/removeMemberToProject/" + idUser, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/*************************************************************************************/
/**************************** Invite Members to join a Project ***********************/
/*************************************************************************************/

export function inviteToJoinProject_Api(token, idProject, idUser, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token: token },
  };

  fetch(generalPath + "projectController/" + idProject + "/inviteToJoinProject/" + idUser, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/*****************************************************************************/
/******************** getAllSkillAssociateProjectAPI - API *******************/
/*****************************************************************************/

export function getAllSkillAssociateProjectAPI(id, token, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token },
  };

  fetch(generalPath + "projectController/skillList/" + id, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/********************************************************************************/
/*********************** getAllInterestAssociateProject - API *******************/
/********************************************************************************/

export function getAllInterestAssociateProjectAPI(id, token, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token },
  };

  fetch(generalPath + "projectController/interestList/" + id, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error);
    });
}

/*********************************************************************************/
/*********************** getAllIdeasNecessityFromProject - API *******************/
/*********************************************************************************/

export function getAllIdeasNecessityFromProjectAPI(id, token, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token },
  };

  fetch(generalPath + "projectController/ideasNecessity/" + id, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/*********************************************************************************/
/************************** RemoveMembersFromProject - API ***********************/
/*********************************************************************************/

export function removeMemberProject(token, idProject, idUser, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token: token },
  };

  fetch(generalPath + "projectController/" + idProject + "/removeMemberProject/" + idUser, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.text();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/************************************************************************************************/
/******************************** Accept/Decline Invite Project - API ***************************/
/************************************************************************************************/

export function acceptInviteToBePartOfProject(token, idProject, acceptOrNot, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token: token },
    body: acceptOrNot
  };

  fetch(generalPath + "projectController/acceptInviteToBePartOfProject/" + idProject, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.text();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/*************************************************************************************************/
/****************************** Aprove member/Or Nor Project request - API ***********************/
/*************************************************************************************************/
export function approveMemberOrNotToBePartOfProject(token, idProject, myJson, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token: token },
    body: myJson
  };

  fetch(generalPath + "projectController/" + idProject + "/approveMemberToProject ", fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.text();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/**************************************************************************/
/**************************** EDIT PROJECT - API **************************/
/**************************************************************************/
export function editProjectApi(token, idProject, data, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { token },
    body: data
  };

  fetch(generalPath + "projectController/editProject/" + idProject, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.text();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/**************************************************************************/
/****************** get All Interests from Project - API ******************/
/**************************************************************************/

export function getAllInterestsFromProjectAPI(id, token, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token: token, },
  };

  fetch(generalPath + "projectController/interestList/" + id, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error);
    });
}

/*********************************************************************************************/
/******************************* associate Interest To Project - API *************************/
/*********************************************************************************************/

export function associateInterestProjectAPI(token, idProject, idInterest, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token: token, },
  };

  fetch(generalPath + "projectController/" + idProject + "/associateInterest?idInterest=" + idInterest, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/***********************************************************************************************/
/*************************** Disassociate Interest To Project - API ****************************/
/***********************************************************************************************/

export function disassociateInterestProjectAPI(token, idProject, idInterest, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token: token, },
  };

  fetch(generalPath + "projectController/" + idProject + "/disassociateInterest?idInterest=" + idInterest, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/**************************************************************************/
/******************** get All Skills from Project - API *******************/
/**************************************************************************/

export function getAllSkillsFromProjectAPI(id, token, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token: token, },
  };

  fetch(generalPath + "projectController/skillList/" + id, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/******************************************************************************************/
/***************************** associate Skill To Project - API ***************************/
/******************************************************************************************/

export function associateSkillProjectAPI(token, idProject, idInterest, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token: token, },
  };

  fetch(generalPath + "projectController/" + idProject + "/associateSkill?idSkill=" + idInterest, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/*********************************************************************************************/
/*************************** Disassociate Skill To Project - API *****************************/
/*********************************************************************************************/

export function disassociateSkillProjectAPI(token, idProject, idInterest, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token: token, },
  };

  fetch(generalPath + "projectController/" + idProject + "/disassociateSkill?idSkill=" + idInterest, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/*******************************************************************************************************/
/********************************** associateIdeaNecessityProjectAPI- API ******************************/
/*******************************************************************************************************/

export function associateIdeaNecessityProjectAPI(token, idProject, idNecessityIdea, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token: token, },
  };

  fetch(generalPath + "projectController/" + idProject + "/associateNecessityIdea?idNecessityIdea=" + idNecessityIdea, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/***********************************************************************************************************/
/*************************************** disassociateIdeaNecessityProjectAPI - API *************************/
/***********************************************************************************************************/

export function disassociateIdeaNecessityProjectAPI(token, idProject, idNecessityIdea, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token: token, },
  };
  fetch(generalPath + "projectController/" + idProject + "/disassociateNecessityIdea?idNecessityIdea=" + idNecessityIdea, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/******************************************************************************************/
/********************** get All Ideas Necessity from Project - API ************************/
/******************************************************************************************/
export function getAllIdeasNecessitiedFromProjectAPI(token, idProject, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token: token, },
  };
  fetch(generalPath + "projectController/ideasNecessity/" + idProject, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/******************************************************************************/
/******************* getfavorite Projects from User - API *********************/
/******************************************************************************/

export function getFavoriteProjectsFromUser(token, idUser, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token: token, },
  };


  fetch(generalPath + "projectController/favorite/" + idUser, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error);
    });
}

/***********************************************************************/
/******************** markProjectAsConcluded - API *********************/
/***********************************************************************/

export function markProjectAsConcludedAPI(token, idProject,  onSuccess) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token },
  };

 
  fetch(generalPath + "projectController/concludeProject/" + idProject, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.text();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      console.log(error)
    });
}

/************** Method of the Controler Groupvisibility ***************/

/**********************************************************************/
/************************* Add a visibility Group *********************/
/**********************************************************************/

export function addaVisibilityGroup(token, myJson, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token },
    body: myJson
  };

  fetch(generalPath + "groupvisibility/setGroup", fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      console.log(error)
    });
}

/******************************************************************/
/************************* Remove Group ***************************/
/******************************************************************/

export function removeGroupAPI(token, idGroup, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token },
    body: idGroup
  };

  fetch(generalPath + "groupvisibility/deleteGroup", fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      console.log(error)
    });
}

/**************** Method of the Controler Post ****************/

/**************************************************************/
/********************* get All Post - API *********************/
/**************************************************************/

export function getAllPostsAPI(id, token, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token },
  };

  fetch(generalPath + "post/allComments/" + id, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/*****************************************************************/
/**************** AddComment - API *******************************/
/*****************************************************************/

export function addCommentAPI(myJSON, token, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token },
    body: myJSON,
  };

  fetch(generalPath + "post/addComment", fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error);
    });
}

/***************************************************************/
/*********************** AddAnswer - API ***********************/
/***************************************************************/

export function addAnswerAPI(myJSON, token, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token },
    body: myJSON,
  };

  fetch(generalPath + "post/addAnswer", fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/********************* Method of the Controller Participation *********************/

/**********************************************************************************/
/******************** get All Projects user is member - API ***********************/
/**********************************************************************************/

export function getAllProjectsMemberFromUserAPI(token, idUser, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token: token },
  };
  fetch(generalPath + "participationController/projectUserMember/" + idUser, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {

      onError(error);
    });
}

/***************************************************************************************/
/************************ Get all Requests Pending answer from A Project ***************/
/***************************************************************************************/

export function findAllProjectRequestsPending_api(token, idProject, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token: token },
  };
  fetch(generalPath + "participationController/invitesPendingProject/" + idProject, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/***************************************************************************************/
/************************ Get all Requests Pending answer from A Project ***************/
/***************************************************************************************/

export function findAllProjectsRequestsPendingAPI(token, idProject, onSuccess, onError) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token: token },
  };

  fetch(generalPath + "participationController/requestsPending/" + idProject, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}

/***************************************************************************/
/********************* deleteParticipation - API ***************************/
/***************************************************************************/

export function deleteParticipationAPI(token, idProject, idUser, onSuccess) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token: token },
  };

  fetch(generalPath + "participationController/deleteParticipation/" + idProject + "/" + idUser, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.text();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
     console.log(error)
    });
}

/***************************************************************************/
/********************* deleteParticipation - API ***************************/
/***************************************************************************/

export function changeParticipationAPI(token, idProject, idUser, onSuccess) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token: token },
  };

  fetch(generalPath + "participationController/changeParticipation/" + idProject + "/" + idUser, fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.text();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
     console.log(error)
    });
}



/**** Method of the Controller SystemManegement ****/

/***************************************************/
/*********** SYSTEM GET TIMEOUT - API **************/
/***************************************************/
export function getTimeoutTimeAPI(token, onSuccess) {
  var fetchProperties = {
    method: "GET",
    headers: { "Content-Type": "application/json", token: token, },
  };

  fetch(generalPath + "system/timeout", fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      console.log(error);
    });
}

/********************************************************************/
/********************* SYSTEM SET TIMEOUT - API *********************/
/********************************************************************/

export function setTimeoutTimeAPI(token, myJson, onSuccess, onError) {
  var fetchProperties = {
    method: "POST",
    headers: { "Content-Type": "application/json", token: token, },
    body: myJson
  };

  fetch(generalPath + "system/timeout", fetchProperties)
    .then(function (response) {
      if (response.ok) {
        return response.text();
      } else {
        throw Error(response.status + ": " + response.statusText);
      }
    })
    .then(function (json) {
      onSuccess(json);
    })
    .catch(function (error) {
      onError(error);
    });
}
