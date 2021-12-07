// Local key storage (key to store in app cache)
export const USER_KEY_STORAGE = '@USERNAME_ASYNC_KEY';
export const USER_ID_KEY_STORAGE = 'CurrentUserID_Key';
export const USER_NICKNAME_KEY_STORAGE = 'CurrentUserNickName_Key';
export const USER_ROLE_KEY_STORAGE = 'CurrentUserRole_Key';
export const USER_BADGE_NUMBER_STORAGE = 'User_Badge_number_@';

// Supported role in the app
export const USER_ROLE = {
  PET_OWNER: 'PetOwner',
  VETERINARIAN: 'Veterinarian'
};

export const SERVER_IP_ADDRESS = 'localhost:8080';
export const REQUEST_URL_PREFIX = 'http://' + SERVER_IP_ADDRESS + '/PawTracker';

// List of URI endpoints used in the app
export const REQUEST_URLS = {

  // Authentication-related
  REGISTER: REQUEST_URL_PREFIX + '/app/register', // PUT
  LOGIN: REQUEST_URL_PREFIX + '/app/login', // POST

  ADD_PET_OWNER: REQUEST_URL_PREFIX + '/users/pet_owner', // POST
  GET_ALL_PET_OWNERS: REQUEST_URL_PREFIX + '/users/pet_owner', // GET

  // Pet registration
  REGISTER_PET: REQUEST_URL_PREFIX + '/pets',  // POST

  // Pet-related
  VIEW_ALL_PETS_DETAILS: REQUEST_URL_PREFIX + '/pets/details' // GET
};