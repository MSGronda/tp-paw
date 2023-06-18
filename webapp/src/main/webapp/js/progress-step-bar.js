//A wrapper to encapsulate all the code
(function (window) {
  function initProgressBar() {
    const ProgressBar = {};
    ProgressBar.singleStepAnimation = 1000; //default value
    // this delay is required as browser will need some time in rendering and then processing css animations.
    const renderingWaitDelay = 200;

    // A utility function to create an element
    const createElement = function (type, className, style, text) {
      const elem = document.createElement(type);
      elem.className = className;
      for (let prop in style) {
        elem.style[prop] = style[prop];
      }
      elem.innerHTML = text;
      return elem;
    };

    const createStatusBar = function (stages, stageWidth, currentStageIndex) {
      const statusBar = createElement('div', 'status-bar', {width: 100 - stageWidth + '%'}, '');
      const currentStatus = createElement('div', 'current-status', {}, '');

      setTimeout(function () {
        currentStatus.style.width = (100 * currentStageIndex) / (stages.length - 1) + '%';
        currentStatus.style.transition = 'width ' + (currentStageIndex * ProgressBar.singleStepAnimation) + 'ms linear';
      }, renderingWaitDelay);

      statusBar.appendChild(currentStatus);
      return statusBar;
    };

    const createCheckPoints = function (stages, stageWidth, currentStageIndex) {
      const ul = createElement('ul', 'progress-bar', {}, '');
      let animationDelay = renderingWaitDelay;
      for (let index = 0; index < stages.length; index++) {
        const li = createElement('li', 'section', {width: stageWidth + '%'}, stages[index]);
        if (currentStageIndex >= index) {
          setTimeout(function (li, currentStageIndex, index) {
            li.className += (currentStageIndex > index) ? ' visited' : ' visited current';
          }, animationDelay, li, currentStageIndex, index);
          animationDelay += ProgressBar.singleStepAnimation;
        }
        ul.appendChild(li);
      }
      return ul;
    };

    const createHTML = function (wrapper, stages, currentStage) {
      const stageWidth = 100 / stages.length;
      const currentStageIndex = stages.indexOf(currentStage);

      //create status bar
      const statusBar = createStatusBar(stages, stageWidth, currentStageIndex);
      wrapper.appendChild(statusBar);

      //create checkpoints
      const checkpoints = createCheckPoints(stages, stageWidth, currentStageIndex);
      wrapper.appendChild(checkpoints);

      return wrapper;
    };

    const validateParameters = function (stages, currentStage, container) {
      if (!(typeof stages === 'object' && stages.length && typeof stages[0] === 'string')) {
        console.error('Expecting Array of strings for "stages" parameter.');
        return false;
      }
      if (typeof currentStage !== 'string') {
        console.error('Expecting string for "current stage" parameter.');
        return false;
      }
      if (typeof container !== 'string' && typeof container !== 'undefined') {
        console.error('Expecting string for "container" parameter.');
        return false;
      }
      return true;
    };

    //exposing this function to user;
    ProgressBar.init = function (stages, currentStage, container) {
      if(validateParameters(stages, currentStage, container)) {
        let wrapper = document.getElementsByClassName(container);
        if(wrapper.length > 0) {
          wrapper = wrapper[0];
        } else {
          wrapper = createElement('div', 'progressbar-wrapper', { }, '');
          document.body.appendChild(wrapper);
        }
        createHTML(wrapper, stages, currentStage);
      }
    };
    return ProgressBar;
  }

  if (typeof ProgressBar === 'undefined') {
    window.ProgressBar = initProgressBar();
  } else {
    console.log('Progress bar loaded');
  }

})(window);