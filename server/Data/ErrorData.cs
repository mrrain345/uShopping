namespace uShopping.Data {
  public class ErrorData {

    public int Code { get; set; }
    public string Title { get; set; }

    public ErrorData(int code, string title) {
      Code = code;
      Title = title;
    }
  }
}