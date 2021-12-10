/**
 * This class contains different common varialbes that will be used throughout the entire project.
 * 
 * @author Putthida Samrith
 * @date 12/9/2021
 */

// Local key storage (unique key to store in app cache)
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

// Server properties
export const SERVER_IP_ADDRESS = '10.0.0.236:8080';
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
  VIEW_PET_BY_RFID: REQUEST_URL_PREFIX + '/pets', // GET
  VIEW_PETS_BY_USER: REQUEST_URL_PREFIX + '/pets/user', // GET - /pet/user/{user_id}
  VIEW_ALL_PETS_DETAILS: REQUEST_URL_PREFIX + '/pets', // GET
  UPDATE_PET_DETAILS: REQUEST_URL_PREFIX + '/pets', // PUT
  DELETE_PET_BY_ID: REQUEST_URL_PREFIX + '/pets', // DELETE

  VIEW_VACCINATION_RECORDS: REQUEST_URL_PREFIX + '/pets',
  VIEW_MEDICAL_RECORDS: REQUEST_URL_PREFIX + '/pets',

  // Locations
  GET_ALL_LOCATIONS_FOR_PET_ID: REQUEST_URL_PREFIX + '/locations', // GET: Get locations of the specified petId
  GET_LATEST_LOCATION_FOR_PET_ID: REQUEST_URL_PREFIX + '/locations', // GET
  GET_CURRENT_LOCATIONS: REQUEST_URL_PREFIX + '/locations/users'
};