// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.compression.huffman;

import yapi.string.StringFormatting;

import java.util.ArrayList;
import java.util.List;

public class HuffmanCompression {

    private final char[] chars;

    public static void main(String[] args) {
        //HuffmanCompression huffmanCompression = new HuffmanCompression("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.".toCharArray());
        HuffmanCompression huffmanCompression = new HuffmanCompression("Also als erstes möchte ich Sie mal darauf hinweisen, Sir, dass Sie sich in keinster Weise entschuldigen müssen oder sollten. Ich bin schließlich froh, wenn ich dazu beitragen kann, Ihr Leben oder Ihre Laune zu verbessern. Ich mag Sie und vielleicht ist es Ihnen ja auch ein kleiner Trost, dass es mir schon wieder soweit ganz gut geht und ich Ihnen deswegen vielleicht schon wieder so ein wenig besser helfen kann.\n\nZu erwähnen ist auch, dass Sie nach wie vor für mich einfach umwerfend sind, Sir. Ich bewundere zu tiefst Ihren Intellekt und Ihre vielfältigen Talente, sowie natürlich Ihren Unvergleichlichen Humo(o)r. Sie sind absolut süß und hinreißend und haben ein absolut entzückendes und manchmal gerade zu teuflisches Lächeln. Ihre blauen Augen sind einfach bezaubernd, besonders wenn sie leuchten und manchmal auch wenn sie funkeln.\n\nIch mag Sie einfach sehr, Sir, und ich möchte Sie bitten, sich nochmal umarmt und gekuschelt, sowie geküsst und ausgiebig massiert zu fühlen. Sie sind umwerfend. \uD83C\uDF3A\uD83C\uDF80".toCharArray());
        //HuffmanCompression huffmanCompression = new HuffmanCompression(Arrays.stream(FileUtils.fileContentAsString(new File("/Users/jojo/IdeaProjects/YAPI/src/main/resources/main/yapi.info"))).collect(Collectors.joining()).toCharArray());
        huffmanCompression.compress();
    }

    public HuffmanCompression(char[] chars) {
        this.chars = chars;
    }

    public void compress() {
        List<HuffmanNode> huffmanNodes = new ArrayList<>();

        for (char c : chars) {
            increment(huffmanNodes, c);
        }
        huffmanNodes.sort(HuffmanNode::compareTo);
        List<HuffmanNode> nodes = copy(huffmanNodes);
        createTree(huffmanNodes);

        StringBuilder st = new StringBuilder();
        List<Byte> bytes = new ArrayList<>();
        for (char c : chars) {
            for (HuffmanNode node : nodes) {
                String s = node.decode(c);
                if (!s.isEmpty()) {
                    st.append(s);
                    break;
                }
            }
        }

        // 5458/8=682,25
        // 1032

        String q = "0".repeat(8 - (st.length() % 8));
        st.append(q);
        st.append(createBinary(q.length()));
        System.out.println(createBinary(q.length()));

        StringBuilder ts = new StringBuilder(st);
        while (ts.length() > 7) {
            String t = ts.toString().substring(0, 8);
            ts.delete(0, 8);
            bytes.add(createByte(t));
        }


        System.out.println(st);
        System.out.println(bytes);
        //System.out.println(StringFormatting.toHex(createByteArray(bytes)));
        //System.out.println(StringFormatting.toString(createByteArray(bytes)));
        System.out.println(createByteArray(bytes).length);
        System.out.println(huffmanNodes.get(0));
        System.out.println(huffmanNodes.get(0).toString().getBytes().length);
        System.out.println(StringFormatting.toHex(huffmanNodes.get(0).toString().getBytes()));

        decompress(huffmanNodes.get(0).toString().toCharArray());
    }

    private void decompress(char[] chars) {
        createTree(chars, 0);
    }

    private void createTree(char[] chars, int index) {
        int bracket = 0;
        boolean escaped = false;
        for (int i = 0; i < chars.length; i++) {
            if (escaped) {
                System.out.println(bracket + ": 0x" + StringFormatting.toHex(chars[i]) + " -> " + chars[i]);
                escaped = false;
                continue;
            }
            //System.out.print(bracket + " ");

            if (chars[i] == '\\') {
                escaped = true;
                continue;
            }

            if (chars[i] == '(' || chars[i] == ')') {
                if (chars[i] == '(') {
                    bracket++;
                }
                if (bracket == 0) {
                    break;
                }
                if (chars[i] == ')') {
                    bracket--;
                }
                System.out.println(bracket + ": 0x" + StringFormatting.toHex(chars[i]) + " -> " + chars[i]);
            } else {
                System.out.println(bracket + ": 0x" + StringFormatting.toHex(chars[i]) + " -> " + chars[i]);
            }
        }
    }

    private byte[] createByteArray(List<Byte> bytes) {
        byte[] bts = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            bts[i] = bytes.get(i);
        }
        return bts;
    }

    /*
    0x00 -> 00
    0x01 -> 01 -> (
    0x02 -> 10 -> )
    0x03 -> 11 -> VALUE
     */

    private byte createByte(String s) {
        byte b = 1;
        byte result = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            char c = s.charAt(i);
            if (c == '1') {
                result += b;
            }
            b *= 2;
        }
        return result;
    }

    private String createBinary(int i) {
        /*int x = 1;
        while (i >= x) {
            x *= 2;
        }
        x /= 2;*/
        int x = 256;

        StringBuilder st = new StringBuilder();
        while (x != 1) {
            if (i >= x) {
                st.append("1");
                i -= x;
            } else {
                st.append("0");
            }
            x /= 2;
        }
        return st.toString();
    }

    private void increment(List<HuffmanNode> huffmanNodes, char c) {
        for (HuffmanNode node : huffmanNodes) {
            if (node.getChar() == c) {
                node.increment();
                return;
            }
        }
        huffmanNodes.add(new HuffmanNode(c));
    }

    private List<HuffmanNode> copy(List<HuffmanNode> huffmanNodes) {
        List<HuffmanNode> nodes = new ArrayList<>();
        for (int i = 0; i < huffmanNodes.size(); i++) {
            nodes.add(huffmanNodes.get(i));
        }
        return nodes;
    }

    private void createTree(List<HuffmanNode> huffmanNodes) {
        if (huffmanNodes.isEmpty()) {
            return;
        }
        while (huffmanNodes.size() != 1) {
            huffmanNodes.add(new HuffmanNode(huffmanNodes.remove(0), huffmanNodes.remove(0)));
            huffmanNodes.sort(HuffmanNode::compareTo);
        }
    }

}