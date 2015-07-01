package techreborn.asm;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModAPIManager;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.versioning.InvalidVersionSpecificationException;
import cpw.mods.fml.common.versioning.VersionRange;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ClassTransformation implements IClassTransformer {

	private static boolean scrappedData = false;
	static  String strippableDesc;
	private static final String[] emptyList = {};

	public ClassTransformation() {
		strippableDesc = Type.getDescriptor(Strippable.class);
	}


	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {

		if (bytes == null) {
			return null;
		}

		ClassReader cr = new ClassReader(bytes);
		ClassNode cn = new ClassNode();
		cr.accept(cn, 0);
		if (strip(cn)) {
			ClassWriter cw = new ClassWriter(0);
			cn.accept(cw);
			bytes = cw.toByteArray();
			System.out.println("Striped " + name);
		} else {
			//LogHelper.trace("Nothing stripped from " + transformedName);
		}

		return bytes;
	}

	static boolean strip(ClassNode cn) {
		boolean altered = false;
		if (cn.visibleAnnotations != null) {
			for (AnnotationNode n : cn.visibleAnnotations) {
				AnnotationInfo node = parseAnnotation(n, strippableDesc);
				if(node != null){

				}
			}
		}
		if (cn.methods != null) {
			Iterator<MethodNode> iter = cn.methods.iterator();
			while (iter.hasNext()) {
				MethodNode mn = iter.next();
				if (mn.visibleAnnotations != null) {
					for (AnnotationNode node : mn.visibleAnnotations) {
						if (checkRemove(parseAnnotation(node, strippableDesc), iter)) {
							altered = true;
							break;
						}
					}
				}
			}
		}
		if (cn.fields != null) {
			Iterator<FieldNode> iter = cn.fields.iterator();
			while (iter.hasNext()) {
				FieldNode fn = iter.next();
				if (fn.visibleAnnotations != null) {
					for (AnnotationNode node : fn.visibleAnnotations) {
						if (checkRemove(parseAnnotation(node, strippableDesc), iter)) {
							altered = true;
							break;
						}
					}
				}
			}
		}
		return altered;
	}


	static AnnotationInfo parseAnnotation(AnnotationNode node, String desc) {

		AnnotationInfo info = null;
		if (node.desc.equals(desc)) {
			info = new AnnotationInfo();
			if (node.values != null) {
				List<Object> values = node.values;
				for (int i = 0, e = values.size(); i < e; ) {
					Object k = values.get(i++);
					Object v = values.get(i++);
					if ("value".equals(k)) {
						if (!(v instanceof List && ((List<?>) v).size() > 0 && ((List<?>) v).get(0) instanceof String)) {
							continue;
						}
						info.values = ((List<?>) v).toArray(emptyList);
					} else if ("side".equals(k) && v instanceof String[]) {
						String t = ((String[]) v)[1];
						if (t != null) {
							info.side = t.toUpperCase().intern();
						}
					}
				}
			}
		}
		return info;
	}

	static boolean checkRemove(AnnotationInfo node, Iterator<? extends Object> iter) {

		if (node != null) {
			boolean needsRemoved = true;
			if (!needsRemoved) {
				String[] value = node.values;
				for (int j = 0, l = value.length; j < l; ++j) {
					String clazz = value[j];
					String mod = clazz.substring(4);
					if (clazz.startsWith("mod:")) {
						int i = mod.indexOf('@');
						if (i > 0) {
							clazz = mod.substring(i + 1);
							mod = mod.substring(0, i);
						}
						needsRemoved = !Loader.isModLoaded(mod);
						if (!needsRemoved && i > 0) {
							ModContainer modc = getLoadedMods().get(mod);
							try {
								needsRemoved = !VersionRange.createFromVersionSpec(clazz).containsVersion(modc.getProcessedVersion());
							} catch (InvalidVersionSpecificationException e) {
								needsRemoved = true;
							}
						}
					}
					if (needsRemoved) {
						break;
					}
				}
			}
			if (needsRemoved) {
				iter.remove();
				return true;
			}
		}
		return false;
	}

	static class AnnotationInfo {

		public String side = "NONE";
		public String[] values = emptyList;
	}

	private static Map<String, ModContainer> mods;
	static Map<String, ModContainer> getLoadedMods() {

		if (mods == null) {
			mods = new HashMap<String, ModContainer>();
			for (ModContainer m : Loader.instance().getModList()) {
				mods.put(m.getModId(), m);
			}
		}
		return mods;
	}
}