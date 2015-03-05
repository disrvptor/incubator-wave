// ===================================================================
//
//   WARNING: GENERATED CODE! DO NOT EDIT!
//
// ===================================================================
/*
 This file generated from:

 /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizationCodePage.gxp
*/

package org.waveprotocol.box.server.gxp;

import com.google.gxp.base.*;
import com.google.gxp.css.*;                                                    // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizationCodePage.gxp: L26, C79
import com.google.gxp.html.*;                                                   // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizationCodePage.gxp: L26, C79
import com.google.gxp.js.*;                                                     // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizationCodePage.gxp: L26, C79
import com.google.gxp.text.*;                                                   // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizationCodePage.gxp: L26, C79

public class OAuthAuthorizationCodePage extends com.google.gxp.base.GxpTemplate {   // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizationCodePage.gxp: L26, C79

  private static final String GXP$MESSAGE_SOURCE = "org.waveprotocol.box.server.gxp";

  public static void write(final java.lang.Appendable gxp$out, final com.google.gxp.base.GxpContext gxp_context, final String code)
      throws java.io.IOException {
    final java.util.Locale gxp_locale = gxp_context.getLocale();
    gxp$out.append("<html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"");   // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizationCodePage.gxp: L30, C3
    if (gxp_context.isUsingXmlSyntax()) {                                       // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizationCodePage.gxp: L33, C3
      gxp$out.append(" /");                                                     // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizationCodePage.gxp: L33, C3
    }
    gxp$out.append(">\n<title>Authorization code</title>\n<link rel=\"shortcut icon\" href=\"/static/favicon.ico\"");   // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizationCodePage.gxp: L33, C3
    if (gxp_context.isUsingXmlSyntax()) {                                       // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizationCodePage.gxp: L35, C3
      gxp$out.append(" /");                                                     // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizationCodePage.gxp: L35, C3
    }
    gxp$out.append("></head>\n<body><div><div>Please copy this code, switch to your application and paste it there:</div>\n<input id=\"code\" type=\"text\" readonly");   // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizationCodePage.gxp: L35, C3
    if (gxp_context.isUsingXmlSyntax()) {                                       // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizationCodePage.gxp: L41, C7
      gxp$out.append("=\"readonly\"");                                          // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizationCodePage.gxp: L41, C7
    }
    gxp$out.append(" style=\"width:500px\" onclick=\"this.focus();this.select();\"");   // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizationCodePage.gxp: L41, C7
    gxp$out.append(" value=\"");                                                // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizationCodePage.gxp: L42, C9
    com.google.gxp.html.HtmlAppender.INSTANCE.append(gxp$out, gxp_context, (code));   // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizationCodePage.gxp: L43, C11
    gxp$out.append("\"");                                                       // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizationCodePage.gxp: L42, C9
    if (gxp_context.isUsingXmlSyntax()) {                                       // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizationCodePage.gxp: L41, C7
      gxp$out.append(" /");                                                     // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizationCodePage.gxp: L41, C7
    }
    gxp$out.append("></div></body></html>");                                    // /Users/guypascarella/Development/git/incubator-wave/src/org/waveprotocol/box/server/gxp/OAuthAuthorizationCodePage.gxp: L41, C7
  }

  private static final java.util.List<String> GXP$ARGLIST = java.util.Collections.unmodifiableList(java.util.Arrays.asList("code"));

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

  public static com.google.gxp.html.HtmlClosure getGxpClosure(final String code) {
    return new TunnelingHtmlClosure() {
      public void writeImpl(final java.lang.Appendable gxp$out, final com.google.gxp.base.GxpContext gxp_context)
          throws java.io.IOException {
        org.waveprotocol.box.server.gxp.OAuthAuthorizationCodePage.write(gxp$out, gxp_context, code);
      }
    };
  }

  /**
   * Interface that defines a strategy for writing this GXP
   */
  public interface Interface {
    public void write(final java.lang.Appendable gxp$out, final com.google.gxp.base.GxpContext gxp_context, final String code)
        throws java.io.IOException;

    public com.google.gxp.html.HtmlClosure getGxpClosure(final String code);
  }

  /**
   * Instantiable instance of this GXP
   */
  public static class Instance implements Interface {

    public Instance() {
    }

    public void write(final java.lang.Appendable gxp$out, final com.google.gxp.base.GxpContext gxp_context, final String code)
        throws java.io.IOException {
      org.waveprotocol.box.server.gxp.OAuthAuthorizationCodePage.write(gxp$out, gxp_context, code);
    }

    public com.google.gxp.html.HtmlClosure getGxpClosure(final String code) {
      return new TunnelingHtmlClosure() {
        public void writeImpl(final java.lang.Appendable gxp$out, final com.google.gxp.base.GxpContext gxp_context)
            throws java.io.IOException {
          Instance.this.write(gxp$out, gxp_context, code);
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
