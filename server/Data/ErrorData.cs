using Microsoft.AspNetCore.Mvc;

namespace uShopping.Data {
  public class ErrorData {

    public int Code { get; set; }
    public string Title { get; set; }

    public ErrorData(int code, string title) {
      Code = code;
      Title = title;
    }

    public ErrorData(string title) {
      Code = 0;
      Title = title;
    }

    public static UnauthorizedObjectResult SessionError() {
      return new UnauthorizedObjectResult(new ErrorData("You're not logged in"));
    }
  }
}