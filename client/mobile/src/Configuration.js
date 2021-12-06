// Local key storage (key to store in app cache)
export const USER_KEY_STORAGE = '@USERNAME_ASYNC_KEY';
export const USER_ID_KEY_STORAGE = 'CurrentUserID_Key';
export const USER_NICKNAME_KEY_STORAGE = 'CurrentUserNickName_Key';
export const USER_ROLE_KEY_STORAGE = 'CurrentUserRole_Key';

// Supported role in the app
export const USER_ROLE = {
  PET_OWNER: 'PetOwner',
  VETERINARIAN: 'Veterinarian'
};

export const SERVER_IP_ADDRESS = 'localhost:8080';
export const REQUEST_URL_PREFIX = 'http://' + SERVER_IP_ADDRESS;

// List of URI endpoints used in the app
export const REQUEST_URLS = {

  // Authentication-related
  REGISTER: REQUEST_URL_PREFIX + '/users',
  LOGIN: REQUEST_URL_PREFIX + '/users/login',

  ADD_PET_OWNER: REQUEST_URL_PREFIX + ''
};