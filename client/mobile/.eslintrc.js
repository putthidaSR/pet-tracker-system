module.exports = {
  "env": {
    "browser": true,
    "react-native/react-native": true
  },
  "extends": [
    "eslint:recommended",
    "plugin:react/recommended"
  ],
  "parser": "@babel/eslint-parser",
  "parserOptions": {
    "ecmaFeatures": {
      "jsx": true
    },
    "ecmaVersion": 12,
    "sourceType": "module"
  },
  "plugins": [
    "react",
    "react-native"
  ],
  "settings": {
    "react": {
      "version": "detect"
    }
  },
  "rules": {
    "react-native/no-unused-styles": 2,
    "react-native/no-inline-styles": 2,
    "react/jsx-uses-react": "error",
    "react/jsx-uses-vars": "error",
    "react/prop-types": 0,
    "comma-dangle": 2,
    "indent": ["error", 2],
    "keyword-spacing": ["error", { "overrides": {
      "if": { "after": true },
      "for": { "after": false },
      "while": { "after": false }
    }}] ,
    "semi": ["error", "always"]
  }
};