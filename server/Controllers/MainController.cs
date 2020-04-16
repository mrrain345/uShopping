using Microsoft.AspNetCore.Mvc;

namespace uShopping.Controllers {

  [ApiController]
  [Route("/")]
  public class MainController : Controller {

    [HttpGet]
    public string Get() {
      return "uShopping";
    }
  }
}