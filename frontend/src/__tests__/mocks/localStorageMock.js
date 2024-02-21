
export default function initializeLocalStorageMock() {
  // Define a mock for localStorage and sessionStorage
  const localStorageMock = (() => {
    let store = {};
    return {
      getItem: key => store[key],
      setItem: (key, value) => { store[key] = value.toString() },
      removeItem: key => { delete store[key] },
      clear: () => { store = {} }
    };
  })();

  global.localStorage = localStorageMock;
  global.sessionStorage = localStorageMock;
}

