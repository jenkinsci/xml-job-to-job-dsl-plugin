package com.adq.jenkins.xmljobtodsl.parsers;

import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLJobStrategy;

import java.io.*;
import java.util.*;

/**
 * Created by alanquintiliano on 20/12/17.
 */
public class DSLTranslator {

		private JobDescriptor[] jobDescriptors;
		private String viewName;
		private List<PropertyDescriptor> notTranslated = new ArrayList<>();

		public DSLTranslator(JobDescriptor[] jobDescriptors, String viewName) throws IOException {
				this.jobDescriptors = jobDescriptors.clone();
				this.viewName = viewName;
		}

		public DSLTranslator(JobDescriptor jobDescriptor, String viewName) throws IOException {
				this(new JobDescriptor[] { jobDescriptor }, viewName);
		}

		public DSLTranslator(JobDescriptor[] jobDescriptors) throws IOException {
				this(jobDescriptors, null);
		}

		public DSLTranslator(JobDescriptor jobDescriptor) throws IOException {
				this(jobDescriptor, null);
		}

		public String toDSL() throws IOException{
				StringBuilder builder = new StringBuilder();
				for (JobDescriptor job : jobDescriptors) {
						System.out.println("\nProcessing job: " + job.getName());
						DSLJobStrategy jobStrategy = new DSLJobStrategy(job);
						try {
							builder.append(jobStrategy.toDSL());
							notTranslated.addAll(jobStrategy.getNotTranslatedList());
						} catch (Exception e) {
							System.out.println("Failed for job: " + job.getName());
							continue;
						}

					System.out.println("\n" + jobStrategy.toDSL());

					if (jobStrategy.getNotTranslatedList().isEmpty()) {
						System.out.println("No untranslated tag! Fit for moving");
						try (Writer writer = new BufferedWriter(new OutputStreamWriter(
								new FileOutputStream("/tmp/" + job.getName().toLowerCase().replaceAll("[.,() -]+", "_") + ".groovy"), "utf-8"))) {
								String tmpJobStrategy = jobStrategy.toDSL().replaceAll("(disabled\\(false\\))", "disabled(true)")
																	       .replaceAll("\"\"\"\"", "\\\\\"\"\"\"");
								writer.write(tmpJobStrategy);
						}
					}
					for (PropertyDescriptor pd : jobStrategy.getNotTranslatedList()) {
						System.out.println(pd.getName());
					}
				}
				if (viewName != null) {
						builder.append(new DSLView(viewName, jobDescriptors).generateViewDSL());
				}
				return builder.toString().trim();
		}

		public List<PropertyDescriptor> getNotTranslated() {
				return notTranslated;
		}
}
