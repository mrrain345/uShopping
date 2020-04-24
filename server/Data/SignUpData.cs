using System;
using System.Runtime.Serialization;
using System.ComponentModel.DataAnnotations;

namespace uShopping.Data {
  public class SignUpData {

    [Required]
    public string Username { get; set; }

    [Required]
    public string Email { get; set; }

    [Required]
    public string Password { get; set; }
    
    [Required]
    public string ConfirmPassword { get; set; }
  }
}