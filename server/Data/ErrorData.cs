using Microsoft.AspNetCore.Mvc;

namespace uShopping.Data {
  public class ErrorData {

    public string Title { get; set; }

    public ErrorData(string title) {
      Title = title;
    }

    public static UnauthorizedObjectResult SessionError() {
      return new UnauthorizedObjectResult(new ErrorData("You're not logged in"));
    }
  }
}