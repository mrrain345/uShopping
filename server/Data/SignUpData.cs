using System;
using System.Runtime.Serialization;
using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace uShopping.Data {
  public class SignUpData {

    [Required]
    public string Username { get; set; }

    [Required]
    public string Email { get; set; }

    [Required]
    public string Password { get; set; }
    
    [Required] [JsonPropertyName("confirm_password")]
    public string ConfirmPassword { get; set; }
  }
}