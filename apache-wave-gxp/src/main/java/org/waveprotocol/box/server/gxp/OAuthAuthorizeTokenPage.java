// ===================================================================
//
//   WARNING: GENERATED CODE! DO NOT EDIT!
//
// ===================================================================
/*
 This file generated from:

 /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizeTokenPage.gxp
*/

package org.waveprotocol.box.server.gxp;

import com.google.gxp.base.*;
import com.google.gxp.css.*;                                                    // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizeTokenPage.gxp: L26, C79
import com.google.gxp.html.*;                                                   // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizeTokenPage.gxp: L26, C79
import com.google.gxp.js.*;                                                     // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizeTokenPage.gxp: L26, C79
import com.google.gxp.text.*;                                                   // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizeTokenPage.gxp: L26, C79

public class OAuthAuthorizeTokenPage extends com.google.gxp.base.GxpTemplate {   // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizeTokenPage.gxp: L26, C79

  private static final String GXP$MESSAGE_SOURCE = "org.waveprotocol.box.server.gxp";

  public static void write(final java.lang.Appendable gxp$out, final com.google.gxp.base.GxpContext gxp_context, final String user, final String xsrfToken)
      throws java.io.IOException {
    final java.util.Locale gxp_locale = gxp_context.getLocale();
    gxp$out.append("<html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"");   // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizeTokenPage.gxp: L31, C3
    if (gxp_context.isUsingXmlSyntax()) {                                       // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizeTokenPage.gxp: L34, C3
      gxp$out.append(" /");                                                     // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizeTokenPage.gxp: L34, C3
    }
    gxp$out.append(">\n<title>Authorization Required</title>\n<link rel=\"shortcut icon\" href=\"/static/favicon.ico\"");   // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizeTokenPage.gxp: L34, C3
    if (gxp_context.isUsingXmlSyntax()) {                                       // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizeTokenPage.gxp: L36, C3
      gxp$out.append(" /");                                                     // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizeTokenPage.gxp: L36, C3
    }
    gxp$out.append("></head>\n<body><div>You logged as ");                      // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizeTokenPage.gxp: L36, C3
    com.google.gxp.html.HtmlAppender.INSTANCE.append(gxp$out, gxp_context, (user));   // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizeTokenPage.gxp: L40, C22
    gxp$out.append(".</div>\nA program would like to access your waves and perform operations on your\nbehalf.\n<form method=\"post\" action=\"\"><input type=\"hidden\" name=\"token\"");   // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizeTokenPage.gxp: L40, C46
    gxp$out.append(" value=\"");                                                // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizeTokenPage.gxp: L45, C3
    com.google.gxp.html.HtmlAppender.INSTANCE.append(gxp$out, gxp_context, (xsrfToken));   // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizeTokenPage.gxp: L46, C5
    gxp$out.append("\"");                                                       // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizeTokenPage.gxp: L45, C3
    if (gxp_context.isUsingXmlSyntax()) {                                       // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizeTokenPage.gxp: L44, C33
      gxp$out.append(" /");                                                     // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizeTokenPage.gxp: L44, C33
    }
    gxp$out.append(">\n<table><tr><input type=\"submit\" value=\"Agree\" name=\"agree\"");   // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizeTokenPage.gxp: L44, C33
    if (gxp_context.isUsingXmlSyntax()) {                                       // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizeTokenPage.gxp: L50, C7
      gxp$out.append(" /");                                                     // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizeTokenPage.gxp: L50, C7
    }
    gxp$out.append(">\n<input type=\"submit\" value=\"Cancel\" name=\"cancel\"");   // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizeTokenPage.gxp: L50, C7
    if (gxp_context.isUsingXmlSyntax()) {                                       // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizeTokenPage.gxp: L51, C7
      gxp$out.append(" /");                                                     // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizeTokenPage.gxp: L51, C7
    }
    gxp$out.append("></tr></table></form></body></html>");                      // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizeTokenPage.gxp: L51, C7
  }

  private static final java.util.List<String> GXP$ARGLIST = java.util.Collections.unmodifiableList(java.util.Arrays.asList("user", "xsrfToken"));

  /**
   * @return the names of the user defined arguments to this template.
   * This is sort of like a mapping between the positional and named
   * parameters. The first two parameters (common to all templates) are
   * not included in this list. (BTW: No, Java reflection does not
   * provide this information)
   */
  public static java.util.List<String> getArgList() {
    return GXP$ARGLIST;
  }

  private abstract static class TunnelingHtmlClosure
      extends GxpTemplate.TunnelingGxpClosure
      implements com.google.gxp.html.HtmlClosure {
  }

  public static com.google.gxp.html.HtmlClosure getGxpClosure(final String user, final String xsrfToken) {
    return new TunnelingHtmlClosure() {
      public void writeImpl(final java.lang.Appendable gxp$out, final com.google.gxp.base.GxpContext gxp_context)
          throws java.io.IOException {
        org.waveprotocol.box.server.gxp.OAuthAuthorizeTokenPage.write(gxp$out, gxp_context, user, xsrfToken);
      }
    };
  }

  /**
   * Interface that defines a strategy for writing this GXP
   */
  public interface Interface {
    public void write(final java.lang.Appendable gxp$out, final com.google.gxp.base.GxpContext gxp_context, final String user, final String xsrfToken)
        throws java.io.IOException;

    public com.google.gxp.html.HtmlClosure getGxpClosure(final String user, final String xsrfToken);
  }

  /**
   * Instantiable instance of this GXP
   */
  public static class Instance implements Interface {

    public Instance() {
    }

    public void write(final java.lang.Appendable gxp$out, final com.google.gxp.base.GxpContext gxp_context, final String user, final String xsrfToken)
        throws java.io.IOException {
      org.waveprotocol.box.server.gxp.OAuthAuthorizeTokenPage.write(gxp$out, gxp_context, user, xsrfToken);
    }

    public com.google.gxp.html.HtmlClosure getGxpClosure(final String user, final String xsrfToken) {
      return new TunnelingHtmlClosure() {
        public void writeImpl(final java.lang.Appendable gxp$out, final com.google.gxp.base.GxpContext gxp_context)
            throws java.io.IOException {
          Instance.this.write(gxp$out, gxp_context, user, xsrfToken);
        }
      };
    }
  }
}

// ===================================================================
//
//   WARNING: GENERATED CODE! DO NOT EDIT!
//
// ===================================================================
