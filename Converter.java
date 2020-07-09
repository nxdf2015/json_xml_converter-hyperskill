package converter;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Converter {
    private Pattern jsonPattern = Pattern.compile("^\\{\"(.+)\"\\s*\\:\\s*(.+)\\}");
    private Pattern xmlPatternTag = Pattern.compile("^(<(.+)/>)");
    private Pattern xmlPatternValue = Pattern.compile("^<(.+)>(.+)</.+>");
    private Pattern xmlPatternValueAttribute = Pattern.compile("^<([^=]+)\\s+(.+)>(.+)</\\1>");
    private Pattern xmlPatternAttribute = Pattern.compile("^<([^=]+) (.+)\\s*/>");
    private Pattern patternAttribute = Pattern.compile("(.+\\s?=\\s?.+\\s+)*(.+\\s?=\\s?.+)");

    public Map<String, String> getAttributes(String attributes) {

        Pattern patternAttributes = Pattern.compile("(\\w+\\s?=\\s?\"[\\w0-9]+\"\\s?)");
        Matcher matcherAttribute = patternAttributes.matcher(attributes);

        Map attributesMap = new HashMap();
        while (matcherAttribute.find()) {
            String group = matcherAttribute.group(1).replaceAll("(\"|\\s)", "");

            String[] items = group.split("\\s?=\\s?");
            attributesMap.put(items[0], items[1]);
        }
        return attributesMap;
    }

    public String toJSON(String line) {
        Pattern xmlPattern = Pattern.compile("^<(?<tag>\\w+)\\s?(?<attributes>.+?)\\s?(/>|>)((?<content>.+?)?</\\k<tag>>)?");
        Matcher matcher = xmlPattern.matcher(line);
        matcher.matches();
        String tag = matcher.group("tag");

        String attributes = matcher.group("attributes");
        String content = matcher.group("content");

        Map<String, String> attributesMap;
        String result = "";

        if (attributes == null) {
            result = String.format("{\"%s\":\"%s\"}", tag, content);

        } else {

            attributesMap = getAttributes(attributes);
            StringBuilder builderAttribute = new StringBuilder();
            for (Map.Entry<String, String> entry : attributesMap.entrySet()) {

                builderAttribute.append(String.format("\"@%s\":\"%s\",", entry.getKey(), entry.getValue()));
            }
            String formatContent = content == null ? "null" : "\"" + content + "\"";
            result += String.format("{\"%1$s\":{%2$s \"#%1$s\":%3$s", tag, builderAttribute.toString(), formatContent) + "}}";
        }

        return result;
    }

    public String toXML(String line) {
        // match line with { tag : content }
        Pattern jsonPattern = Pattern.compile("\\{.+?\"(?<tag>.+?)\" : \\{?(?<content>.+)\\}?.+?\\}");

        // match content with { @attribute : value #content : value }
        Pattern attributePattern = Pattern.compile("\"(?<key>(#|@)\\w+)\"\\s:\\s\"?(?<value>[\\w\\d]+)\"?");

        Matcher matcher = jsonPattern.matcher(line);
        matcher.matches();

        String tag = matcher.group("tag").trim();
        String contentElt = matcher.group("content");


        matcher = attributePattern.matcher(contentElt);
        StringBuilder builder = new StringBuilder("");
        String content = "";
        if ((matcher.find())) {

            boolean next;

            do {
                String key = matcher.group("key");
                String value = matcher.group("value");

                if (key.startsWith("@")) {
                    builder.append(String.format("%s = \"%s\"", key.substring(1, key.length()), value));
                } else {
                    content = value;
                }
                next = matcher.find();
                if (next && !matcher.group("key").startsWith("#")) {
                    builder.append(" ");
                }

            } while (next);
        } else {
            content = contentElt.replaceAll("\"", "");
        }


        String attributes = builder.toString();
        String result = String.format("<%s", tag);
        if (!attributes.isEmpty())
            result += String.format(" %s", attributes);

        if (content.equals("null")) {
            result += "/>";
        } else {
            result += ">" + content + String.format("</%s>", tag);
        }
        return result;
    }


}