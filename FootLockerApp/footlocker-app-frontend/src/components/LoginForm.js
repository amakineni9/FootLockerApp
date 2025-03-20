import React from 'react'

function LoginForm() {
    return (
      <React.Fragment>
        <label for="username">USERNAME</label>
        <input type="text" id="username" />
        <label for="password">PASSWORD</label>
        <input type="text" id="password" />
        <input type="submit" value="submit" className="submit" />
      </React.Fragment>
    );
  }

  export default LoginForm;