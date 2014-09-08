package nc.noumea.mairie.kiosque.export.excel;

import java.text.AttributedCharacterIterator;
import java.text.CharacterIterator;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Sam
 * 
 */
public class DateParser {
	private static final String SPACES = "[ \\t]*";
	private static final String SPACE1 = "[ \\t]+";
	private static final String START_ANCHOR = "^" + SPACES;
	private static final String COMMA = SPACES + "[,]" + SPACE1;
	private static final String DASH_SLASH_SPACE = "(?:" + SPACES + "[-/]?|[-/]?)" + SPACES; // dash,
																								// slash,
																								// or
																								// space
	private static final String DOT_DASH_SLASH_SPACE = "(?:" + SPACES + "[-/.]?|[-/.]?)" + SPACES; // dot,
																									// dash,
																									// slash,
																									// or
																									// space
	private static final String DOT_DASH_SLASH = SPACES + "[-/.]" + SPACES; // dot,
																			// dash,
																			// or
																			// slash
	private static final String DASH_SLASH = SPACES + "[/-]" + SPACES; // dash
																		// or
																		// slash
	private static final String COLON_SPACE = "(?:" + SPACES + "[:]?|[:]?)" + SPACES; // colon
																						// or
																						// space
	private static final String DOT_SPACE = "(?:" + SPACES + "[.]?|[.]?)" + SPACES; // dot
																					// or
																					// space
	private static final String NUM_MONTH_G = "([0]?[1-9]|1[012])"; // Numbed
																	// Month: 1
																	// ~ 12
	private static final String DAY_G = "([0]?[1-9]|[12][0-9]|3[01])"; // Day: 1
																		// ~ 31
	private static final String YEAR_G = "(19\\d\\d|[2-9]\\d\\d\\d|\\d\\d|\\d)"; // 1900
																					// -
																					// 9999
	private static final String NAME_MONTH_G = "([A-Z\\p{InLatin-1Supplement}]{3,9})"; // Named
																						// Month:
																						// Jan,
																						// Feb,
																						// Mar,
																						// Apr,
																						// May,
																						// Jun,
																						// Jul,
																						// Aug,
																						// Sep,
																						// Oct,
																						// Nov,
																						// Dec
	private static final String END_ANCHOR = SPACES + "$";
	private static final String AMPM = "[AP]M?";
	private static final String AMPM1_G = "(" + AMPM + ")";
	private static final String COLON_AMPM = "(?:" + SPACES + "[:]|" + SPACE1 + AMPM1_G + ")" + SPACES; // colon
																										// or
																										// am/pm
	private static final String HOUR_G = "(\\d{1,4})";
	private static final String MINUTE_G = "(\\d{1,4})";
	private static final String SECOND_G = "(\\d{0,4})";
	private static final String MSECOND_G = "(\\d{0,4})";
	private static final String AMPM2_G = "(" + SPACE1 + AMPM + "|" + AMPM + ")?";
	private static final String TIME = HOUR_G + COLON_AMPM + "(?:" + MINUTE_G + COLON_SPACE + "|" + SPACES + ")"
			+ SECOND_G + DOT_SPACE + MSECOND_G + AMPM2_G;
	private static final String TIME_PAT = START_ANCHOR + TIME + END_ANCHOR;
	private static final int flags = Pattern.CASE_INSENSITIVE;
	private final ArrayList<FormatInfo> _formatInfos = new ArrayList<FormatInfo>(15);

	public DateParser(Locale locale) {
		final String[] date11 = getDate11(locale, false);
		_formatInfos.add(new FormatInfo(Pattern.compile(date11[0], flags), "d-mmm-yy", date11[1], null, locale));
		final String[] date12 = getDate12(locale, false);
		_formatInfos.add(new FormatInfo(Pattern.compile(date12[0], flags), "d-mmm-yy", date12[1], null, locale));
		final String[] date21 = getDate21(locale, false);
		_formatInfos.add(new FormatInfo(Pattern.compile(date21[0], flags), "d-mmm", date21[1], null, locale));
		final String[] date22 = getDate22(locale, false);
		_formatInfos.add(new FormatInfo(Pattern.compile(date22[0], flags), "d-mmm", date22[1], null, locale));
		final String[] date31 = getDate31(locale, false);
		_formatInfos.add(new FormatInfo(Pattern.compile(date31[0], flags), "mmm-yy", date31[1], null, locale));
		final String[] date32 = getDate32(locale, false);
		_formatInfos.add(new FormatInfo(Pattern.compile(date32[0], flags), "mmm-yy", date32[1], null, locale));
		final String[] date4 = getDate4(locale, false);
		_formatInfos.add(new FormatInfo(Pattern.compile(date4[0], flags), "m/d/yyyy", date4[1], null, locale));
		_formatInfos.add(new FormatInfo(Pattern.compile(TIME_PAT, flags), null, "2:3:5.100 A", "2 P", locale));
		final String[] datetime11 = getDate11(locale, true);
		_formatInfos.add(new FormatInfo(Pattern.compile(datetime11[0], flags), "m/d/yyyy h:mm", datetime11[1],
				datetime11[2], locale));
		final String[] datetime12 = getDate12(locale, true);
		_formatInfos.add(new FormatInfo(Pattern.compile(datetime12[0], flags), "m/d/yyyy h:mm", datetime12[1],
				datetime12[2], locale));
		final String[] datetime21 = getDate21(locale, true);
		_formatInfos.add(new FormatInfo(Pattern.compile(datetime21[0], flags), "m/d/yyyy h:mm", datetime21[1],
				datetime21[2], locale));
		final String[] datetime22 = getDate22(locale, true);
		_formatInfos.add(new FormatInfo(Pattern.compile(datetime22[0], flags), "m/d/yyyy h:mm", datetime22[1],
				datetime22[2], locale));
		final String[] datetime31 = getDate31(locale, true);
		_formatInfos.add(new FormatInfo(Pattern.compile(datetime31[0], flags), "m/d/yyyy h:mm", datetime31[1],
				datetime31[2], locale));
		final String[] datetime32 = getDate32(locale, true);
		_formatInfos.add(new FormatInfo(Pattern.compile(datetime32[0], flags), "m/d/yyyy h:mm", datetime32[1],
				datetime32[2], locale));
		final String[] datetime4 = getDate4(locale, true);
		_formatInfos.add(new FormatInfo(Pattern.compile(datetime4[0], flags), "m/d/yyyy h:mm", datetime4[1],
				datetime4[2], locale));
	}

	/**
	 * Returns the date if string can parse to date, otherwise throw
	 * {@link ParseDateException}
	 * 
	 * @return
	 * @throws ParseDateException
	 */
	public Date parseToDate(String txt) throws ParseDateException {
		for (FormatInfo info : _formatInfos) {
			Matcher m = info.getMaskPattern().matcher(txt);
			if (m.matches()) {
				return info.parseInput(txt, m, -1);
			}
		}
		throw new ParseDateException();
	}

	// "JUL 18, 65"
	String[] getDate11(Locale locale, boolean withTime) {
		DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
		final TimeZone gmt = TimeZone.getTimeZone("GMT");
		format.setTimeZone(gmt);
		final Calendar cal = Calendar.getInstance(gmt);
		cal.set(65, 6, 18, 0, 0, 0);
		final AttributedCharacterIterator iter = format.formatToCharacterIterator(cal.getTime());
		final StringBuilder sb = new StringBuilder(); // generated regular
														// expression
		final StringBuilder sb1 = new StringBuilder(); // sample input text
		boolean alreadyDate = false;
		boolean alreadyMonth = false;
		boolean alreadyYear = false;
		int leadingSpace = -2;
		int leadingComma = -2;
		int j = 0;
		sb.append(START_ANCHOR);
		for (char c = iter.first(); c != CharacterIterator.DONE; c = iter.next()) {
			final Map attrs = iter.getAttributes();
			if (attrs.isEmpty()) {
				switch (c) {
					case ',': // comma
						leadingComma = j;
						break;
					case ' ': // space
						if (leadingComma == (j - 1)) {
							sb.append(COMMA);
						} else if (leadingSpace != (j - 1)) {
							sb.append(DASH_SLASH_SPACE);
						}
						leadingSpace = j;
						break;
					case '.': // dot
					case '-': // dash //ZSS-122: in Japan medium DateFormat is
								// 1965/07/18
					case '/': // slash
						if (leadingSpace != (j - 1)) {
							sb.append(DOT_DASH_SLASH_SPACE);
						}
						leadingSpace = j;
				}
				sb1.append(c);
			} else {
				for (DateFormat.Field field : (Set<DateFormat.Field>) attrs.keySet()) {
					switch (field.getCalendarField()) {
						case Calendar.DAY_OF_MONTH:
							if (!alreadyDate) {
								alreadyDate = true;
								sb.append(DAY_G);
								sb1.append("18");
							}
							break;
						case Calendar.MONTH:
							if (!alreadyMonth) {
								alreadyMonth = true;
								sb.append(NAME_MONTH_G);
								sb1.append("JUL");
							}
							break;
						case Calendar.YEAR:
							if (!alreadyYear) {
								alreadyYear = true;
								sb.append(YEAR_G);
								sb1.append("65");
							}
							break;
					}
				}
			}
			++j;
		}
		final StringBuilder sb2 = new StringBuilder(); // reqular expression
														// with time part if any
		if (withTime) {
			sb.append(SPACE1).append(TIME);
			sb2.append(sb1).append(" 2 P");
			sb1.append(" 2:3:5.100 A");
		}
		sb.append(END_ANCHOR);
		return withTime ? new String[] { sb.toString(), sb1.toString(), sb2.toString() } : new String[] {
				sb.toString(), sb1.toString() };
	}

	private void enforceDMY(StringBuilder sb, StringBuilder sb1, int attrCount) {
		switch (attrCount) {
			case 0: // date
				sb.append(DAY_G);
				sb1.append("18");
				break;
			case 1: // month
				sb.append(NAME_MONTH_G);
				sb1.append("JUL");
				break;
			case 2: // year
				sb.append(YEAR_G);
				sb1.append("65");
				break;
		}
	}

	// "JUL 18, 65" or "18 JUL 65"
	private String[] getDate12(Locale locale, boolean withTime) {
		DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, locale);
		final TimeZone gmt = TimeZone.getTimeZone("GMT");
		format.setTimeZone(gmt);
		final Calendar cal = Calendar.getInstance(gmt);
		cal.set(65, 6, 18, 0, 0, 0);
		final AttributedCharacterIterator iter = format.formatToCharacterIterator(cal.getTime());
		final StringBuilder sb = new StringBuilder();
		final StringBuilder sb1 = new StringBuilder();
		boolean alreadyDate = false;
		boolean alreadyMonth = false;
		boolean alreadyYear = false;
		int leadingSpace = -2;
		int attrCount = 0;
		int j = 0;
		sb.append(START_ANCHOR);
		for (char c = iter.first(); c != CharacterIterator.DONE && attrCount < 3; c = iter.next()) {
			final Map attrs = iter.getAttributes();
			if (attrs.isEmpty()) {
				if (leadingSpace != (j - 1)) {
					if (c == '.') {
						sb.append(DOT_DASH_SLASH_SPACE);
					} else {
						sb.append(DASH_SLASH_SPACE);
					}
					sb1.append(' ');
				}
				leadingSpace = j;
			} else {
				for (DateFormat.Field field : (Set<DateFormat.Field>) attrs.keySet()) {
					switch (field.getCalendarField()) {
						case Calendar.DAY_OF_MONTH:
							if (!alreadyDate) {
								alreadyDate = true;
								enforceDMY(sb, sb1, attrCount);
								++attrCount;
							}
							break;
						case Calendar.MONTH:
							if (!alreadyMonth) {
								alreadyMonth = true;
								enforceDMY(sb, sb1, attrCount);
								++attrCount;
							}
							break;
						case Calendar.YEAR:
							if (!alreadyYear) {
								alreadyYear = true;
								enforceDMY(sb, sb1, attrCount);
								++attrCount;
							}
							break;
					}
				}
			}
			++j;
		}
		final StringBuilder sb2 = new StringBuilder();
		if (withTime) {
			sb.append(SPACE1).append(TIME);
			sb2.append(sb1).append(" 2 P");
			sb1.append(" 2:3:5.100 A");
		}
		sb.append(END_ANCHOR);
		return withTime ? new String[] { sb.toString(), sb1.toString(), sb2.toString() } : new String[] {
				sb.toString(), sb1.toString() };
	}

	// "JUL 18" or "18 JUL": (M-d), (M-y), (d-M), (y-M)
	private String[] getDate21(Locale locale, boolean withTime) {
		DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, locale);
		final TimeZone gmt = TimeZone.getTimeZone("GMT");
		format.setTimeZone(gmt);
		final Calendar cal = Calendar.getInstance(gmt);
		cal.set(65, 6, 18, 0, 0, 0);
		final AttributedCharacterIterator iter = format.formatToCharacterIterator(cal.getTime());
		final StringBuilder sb = new StringBuilder();
		final StringBuilder sb1 = new StringBuilder();
		boolean alreadyDate = false;
		boolean alreadyMonth = false;
		boolean alreadyYear = false;
		int attrCount = 0;
		int leadingSpace = -2;
		int j = 0;
		sb.append(START_ANCHOR);
		for (char c = iter.first(); c != CharacterIterator.DONE && attrCount < 2; c = iter.next()) {
			final Map attrs = iter.getAttributes();
			if (attrs.isEmpty()) {
				if (leadingSpace != (j - 1)) {
					if (c == '.') {
						sb.append(DOT_DASH_SLASH_SPACE);
					} else {
						sb.append(DASH_SLASH_SPACE);
					}
					sb1.append(' ');
				}
				leadingSpace = j;
			} else {
				for (DateFormat.Field field : (Set<DateFormat.Field>) attrs.keySet()) {
					switch (field.getCalendarField()) {
						case Calendar.DAY_OF_MONTH:
							if (!alreadyDate) {
								alreadyDate = true;
								sb.append(DAY_G);
								sb1.append("18");
								++attrCount;
							}
							break;
						case Calendar.MONTH:
							if (!alreadyMonth) {
								alreadyMonth = true;
								sb.append(NAME_MONTH_G);
								sb1.append("JUL");
								++attrCount;
							}
							break;
						case Calendar.YEAR:
							if (!alreadyYear) {
								alreadyYear = true;
								sb.append(YEAR_G);
								sb1.append("65");
								++attrCount;
							}
							break;
					}
				}
			}
			++j;
		}
		final StringBuilder sb2 = new StringBuilder();
		if (withTime) {
			sb.append(SPACE1).append(TIME);
			sb2.append(sb1).append(" 2 P");
			sb1.append(" 2:3:5.100 A");
		}
		sb.append(END_ANCHOR);
		return withTime ? new String[] { sb.toString(), sb1.toString(), sb2.toString() } : new String[] {
				sb.toString(), sb1.toString() };
	}

	// "7-18": (M-d), (M-y), (d-M), (y-M)
	private String[] getDate22(Locale locale, boolean withTime) {
		DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, locale);
		final TimeZone gmt = TimeZone.getTimeZone("GMT");
		format.setTimeZone(gmt);
		final Calendar cal = Calendar.getInstance(gmt);
		cal.set(65, 6, 18, 0, 0, 0);
		final AttributedCharacterIterator iter = format.formatToCharacterIterator(cal.getTime());
		final StringBuilder sb = new StringBuilder();
		final StringBuilder sb1 = new StringBuilder();
		boolean alreadyDate = false;
		boolean alreadyMonth = false;
		boolean alreadyYear = false;
		int leadingSpace = -2;
		int j = 0;
		int attrCount = 0;
		sb.append(START_ANCHOR);
		for (char c = iter.first(); c != CharacterIterator.DONE && attrCount < 2; c = iter.next()) {
			final Map attrs = iter.getAttributes();
			if (attrs.isEmpty()) {
				if (leadingSpace != (j - 1)) {
					if ('.' == c) {
						sb.append(DOT_DASH_SLASH);
					} else {
						sb.append(DASH_SLASH);
					}
					sb1.append('-');
				}
				leadingSpace = j;
			} else {
				for (DateFormat.Field field : (Set<DateFormat.Field>) attrs.keySet()) {
					switch (field.getCalendarField()) {
						case Calendar.DAY_OF_MONTH:
							if (!alreadyDate) {
								alreadyDate = true;
								sb.append(DAY_G);
								sb1.append("18");
								++attrCount;
							}
							break;
						case Calendar.MONTH:
							if (!alreadyMonth) {
								alreadyMonth = true;
								sb.append(NUM_MONTH_G);
								sb1.append("7");
								++attrCount;
							}
							break;
						case Calendar.YEAR:
							if (!alreadyYear) {
								alreadyYear = true;
								sb.append(YEAR_G);
								sb1.append("65");
								++attrCount;
							}
							break;
					}
				}
			}
			++j;
		}
		final StringBuilder sb2 = new StringBuilder();
		if (withTime) {
			sb.append(SPACE1).append(TIME);
			sb2.append(sb1).append(" 2 P");
			sb1.append(" 2:3:5.100 A");
		}
		sb.append(END_ANCHOR);
		return withTime ? new String[] { sb.toString(), sb1.toString(), sb2.toString() } : new String[] {
				sb.toString(), sb1.toString() };
	}

	// "JUL 65": supplement format for date21. That is, if date21 return (M-d),
	// then have to check (M-y)
	private String[] getDate31(Locale locale, boolean withTime) {
		DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, locale);
		final TimeZone gmt = TimeZone.getTimeZone("GMT");
		format.setTimeZone(gmt);
		final Calendar cal = Calendar.getInstance(gmt);
		cal.set(65, 6, 18, 0, 0, 0);
		final AttributedCharacterIterator iter = format.formatToCharacterIterator(cal.getTime());
		final StringBuilder sb = new StringBuilder();
		final StringBuilder sb1 = new StringBuilder();
		boolean alreadyDate = false;
		boolean alreadyMonth = false;
		boolean alreadyYear = false;
		int attrCount = 0;
		int leadingSpace = -2;
		int j = 0;
		sb.append(START_ANCHOR);
		for (char c = iter.first(); c != CharacterIterator.DONE && attrCount < 2; c = iter.next()) {
			final Map attrs = iter.getAttributes();
			if (attrs.isEmpty()) {
				if (leadingSpace != (j - 1) && alreadyMonth) {
					if (c == '.') {
						sb.append(DOT_DASH_SLASH_SPACE);
					} else {
						sb.append(DASH_SLASH_SPACE);
					}
					sb1.append(' ');
				}
				leadingSpace = j;
			} else {
				for (DateFormat.Field field : (Set<DateFormat.Field>) attrs.keySet()) {
					switch (field.getCalendarField()) {
						case Calendar.DAY_OF_MONTH:
							if (!alreadyDate && alreadyMonth) {
								alreadyDate = true;
								sb.append(YEAR_G); // enforce year after month
													// format
								sb1.append("65");
								++attrCount;
							}
							break;
						case Calendar.MONTH:
							if (!alreadyMonth) {
								alreadyMonth = true;
								sb.append(NAME_MONTH_G);
								sb1.append("JUL");
								++attrCount;
							}
							break;
						case Calendar.YEAR:
							if (!alreadyYear && alreadyMonth) {
								alreadyYear = true;
								sb.append(YEAR_G);
								sb1.append("65");
								++attrCount;
							}
							break;
					}
				}
			}
			++j;
		}
		final StringBuilder sb2 = new StringBuilder();
		if (withTime) {
			sb.append(SPACE1).append(TIME);
			sb2.append(sb1).append(" 2 P");
			sb1.append(" 2:3:5.100 A");
		}
		sb.append(END_ANCHOR);
		return withTime ? new String[] { sb.toString(), sb1.toString(), sb2.toString() } : new String[] {
				sb.toString(), sb1.toString() };
	}

	// "7-65": supplement format for date22. That is, if date22 return (M-d),
	// (d-M), or (y-M), then have to check (M-y)
	private String[] getDate32(Locale locale, boolean withTime) {
		DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, locale);
		final TimeZone gmt = TimeZone.getTimeZone("GMT");
		format.setTimeZone(gmt);
		final Calendar cal = Calendar.getInstance(gmt);
		cal.set(65, 6, 18, 0, 0, 0);
		final AttributedCharacterIterator iter = format.formatToCharacterIterator(cal.getTime());
		final StringBuilder sb = new StringBuilder();
		final StringBuilder sb1 = new StringBuilder();
		boolean alreadyDate = false;
		boolean alreadyMonth = false;
		boolean alreadyYear = false;
		int leadingSpace = -2;
		int j = 0;
		int attrCount = 0;
		sb.append(START_ANCHOR);
		for (char c = iter.first(); c != CharacterIterator.DONE && attrCount < 2; c = iter.next()) {
			final Map attrs = iter.getAttributes();
			if (attrs.isEmpty()) {
				if (leadingSpace != (j - 1) && attrCount < 2 && alreadyMonth) {
					if ('.' == c) {
						sb.append(DOT_DASH_SLASH);
					} else {
						sb.append(DASH_SLASH);
					}
					sb1.append('-');
				}
				leadingSpace = j;
			} else {
				for (DateFormat.Field field : (Set<DateFormat.Field>) attrs.keySet()) {
					switch (field.getCalendarField()) {
						case Calendar.DAY_OF_MONTH:
							if (!alreadyDate && alreadyMonth) {
								alreadyDate = true;
								sb.append(YEAR_G); // enforce year after month
													// format
								sb1.append("65");
								++attrCount;
							}
							break;
						case Calendar.MONTH:
							if (!alreadyMonth) {
								alreadyMonth = true;
								sb.append(NUM_MONTH_G);
								sb1.append("7");
								++attrCount;
							}
							break;
						case Calendar.YEAR:
							if (!alreadyYear && alreadyMonth) {
								alreadyYear = true;
								sb.append(YEAR_G);
								sb1.append("65");
								++attrCount;
							}
							break;
					}
				}
			}
			++j;
		}
		final StringBuilder sb2 = new StringBuilder();
		if (withTime) {
			sb.append(SPACE1).append(TIME);
			sb2.append(sb1).append(" 2 P");
			sb1.append(" 2:3:5.100 A");
		}
		sb.append(END_ANCHOR);
		return withTime ? new String[] { sb.toString(), sb1.toString(), sb2.toString() } : new String[] {
				sb.toString(), sb1.toString() };
	}

	// "7-18-65": (M-d-y), (d-M-y), or (y-M-d)
	private String[] getDate4(Locale locale, boolean withTime) {
		DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, locale);
		final TimeZone gmt = TimeZone.getTimeZone("GMT");
		format.setTimeZone(gmt);
		final Calendar cal = Calendar.getInstance(gmt);
		cal.set(65, 6, 18, 0, 0, 0);
		final AttributedCharacterIterator iter = format.formatToCharacterIterator(cal.getTime());
		final StringBuilder sb = new StringBuilder();
		final StringBuilder sb1 = new StringBuilder();
		boolean alreadyDate = false;
		boolean alreadyMonth = false;
		boolean alreadyYear = false;
		int leadingSpace = -2;
		int j = 0;
		sb.append(START_ANCHOR);
		for (char c = iter.first(); c != CharacterIterator.DONE; c = iter.next()) {
			final Map attrs = iter.getAttributes();
			if (attrs.isEmpty()) {
				if (leadingSpace != (j - 1)) {
					if (c == '.') {
						sb.append(DOT_DASH_SLASH);
					} else {
						sb.append(DASH_SLASH);
					}
					sb1.append('-');
				}
				leadingSpace = j;
			} else {
				for (DateFormat.Field field : (Set<DateFormat.Field>) attrs.keySet()) {
					switch (field.getCalendarField()) {
						case Calendar.DAY_OF_MONTH:
							if (!alreadyDate) {
								alreadyDate = true;
								sb.append(DAY_G);
								sb1.append("18");
							}
							break;
						case Calendar.MONTH:
							if (!alreadyMonth) {
								alreadyMonth = true;
								sb.append(NUM_MONTH_G);
								sb1.append("7");
							}
							break;
						case Calendar.YEAR:
							if (!alreadyYear) {
								alreadyYear = true;
								sb.append(YEAR_G);
								sb1.append("65");
							}
							break;
					}
				}
			}
			++j;
		}
		final StringBuilder sb2 = new StringBuilder();
		if (withTime) {
			sb.append(SPACE1).append(TIME);
			sb2.append(sb1).append(" 2 P");
			sb1.append(" 2:3:5.100 A");
		}
		sb.append(END_ANCHOR);
		return withTime ? new String[] { sb.toString(), sb1.toString(), sb2.toString() } : new String[] {
				sb.toString(), sb1.toString() };
	}

	private static class FormatInfo {
		private final Pattern _mask;
		private final String _format;
		private int _year;
		private int _month;
		private int _day;
		private int _hour;
		private int _minute;
		private int _second;
		private int _msecond;
		private int _ampm1;
		private int _ampm2;
		private Locale _locale;

		public FormatInfo(Pattern mask, String format, String groupMatchText, String ampm1MatchText, Locale locale) {
			_locale = locale;
			_mask = mask;
			_format = format;
			Matcher m = _mask.matcher(groupMatchText);
			if (!m.matches()) {
				throw new RuntimeException("Wrong groupMatchText: " + groupMatchText + ", regex mask: " + _mask);
			}
			for (int j = 1, len = m.groupCount() + 1; j < len; ++j) {
				final String grptxt = m.group(j);
				if ("65".equals(grptxt)) {
					_year = j;
				} else if ("7".equals(grptxt)) {
					_month = j;
				} else if ("JUL".equals(grptxt)) {
					_month = j;
				} else if ("18".equals(grptxt)) {
					_day = j;
				} else if (" A".equals(grptxt)) {
					_ampm2 = j;
				} else if ("2".equals(grptxt)) {
					_hour = j;
				} else if ("3".equals(grptxt)) {
					_minute = j;
				} else if ("5".equals(grptxt)) {
					_second = j;
				} else if ("100".equals(grptxt)) {
					_msecond = j;
				}
			}
			if (ampm1MatchText != null) {
				m = _mask.matcher(ampm1MatchText);
				if (!m.matches()) {
					throw new RuntimeException("Wrong ampm1MatchText: " + ampm1MatchText + ", regex mask: " + _mask);
				}
				for (int j = 1, len = m.groupCount(); j < len; ++j) {
					final String grptxt = m.group(j);
					if ("P".equals(grptxt)) {
						_ampm1 = j;
						break;
					}
				}
			}
		}

		public Pattern getMaskPattern() {
			return _mask;
		}

		public String getFormat() {
			return _format;
		}

		// TODO
		private int getMonthIndex(String month) {
			month = month.toUpperCase();
			FullMonthData fmd = FullMonthData.getInstance(CircularData.UPPER, _locale);
			return fmd.getIndexByStartsWith(month);
		}

		// return month index (January is 0)
		private int parseMonth(Matcher m) {
			if (_month > 0) {
				final String month = m.group(_month);
				if (month.length() >= 3) { // Feb case
					return getMonthIndex(month);
				} else {
					return Integer.parseInt(month) - 1;
				}
			}
			return -1;
		}

		// return day index (1st day is 1)
		private int parseDay(Matcher m) {
			if (_day > 0) {
				final String day = m.group(_day);
				return Integer.parseInt(day);
			} else if (_day == 0) { // no day part
				return 1; // default to first day
			}
			return -1;
		}

		// return year
		private int parseYear(Matcher m, int twoDigitYearUpperBound) {
			if (_year > 0) {
				final String year = m.group(_year);
				int y = Integer.parseInt(year);
				return y < 100 ? twoDigitYearTo4DigitYear(y, twoDigitYearUpperBound) : y;
			}
			return -1;
		}

		private Calendar getCalendar() {
			return Calendar.getInstance();
		}

		private int twoDigitYearTo4DigitYear(int y, int twoDigitYearUpperBound) {
			if (twoDigitYearUpperBound < 1999) {
				twoDigitYearUpperBound = (getCalendar().get(Calendar.YEAR) / 10) * 10 + 20 - 1;
			}
			final int upperLowDigit = twoDigitYearUpperBound % 100;
			if (y <= upperLowDigit) {
				return twoDigitYearUpperBound - upperLowDigit + y;
			} else {
				return twoDigitYearUpperBound - upperLowDigit - 100 + y;
			}
		}

		private static final int[] MAXDAYS = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

		private boolean isLeapYear(int year) {
			return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
		}

		private int getMaxday(int month, int year) {
			int maxday = MAXDAYS[month];
			if (month == 1 && isLeapYear(year)) {// Feb index = 1
				return maxday + 1;
			}
			return maxday;
		}

		private int parseHour(Matcher m) {
			return parseTime(m, _hour);
		}

		// return minute number
		private int parseMinute(Matcher m) {
			return parseTime(m, _minute);
		}

		// return second number
		private int parseSecond(Matcher m) {
			return parseTime(m, _second);
		}

		// return millsecnd number
		private int parseMillisecond(Matcher m) {
			return parseTime(m, _msecond);
		}

		// return minute number
		private int parseTime(Matcher m, int groupIndex) {
			if (groupIndex > 0) {
				final String value = m.group(groupIndex);
				if (value != null && value.length() > 0) {
					return Integer.parseInt(value);
				}
			}
			return -1;
		}

		// return ampm
		private String parseAmpm(Matcher m) {
			String result = null;
			if (_ampm2 > 0) {
				result = m.group(_ampm2);
			}
			if (result == null && _ampm1 > 0) {
				result = m.group(_ampm1);
			}
			return result;
		}

		public Date parseInput(String txt, Matcher m, int twoDigitYearUpperBound) throws ParseDateException {
			int month = parseMonth(m);
			if (month < 0 && _month > 0) {
				throw new ParseDateException();
			}
			int day = parseDay(m);
			if (day < 0 && _day > 0) {
				throw new ParseDateException();
			}
			final Calendar cal = getCalendar();
			int year = parseYear(m, twoDigitYearUpperBound);
			if (year < 0 && _format != null) {
				year = cal.get(Calendar.YEAR);
			}
			String format = _format;
			if (_month > 0) {
				int maxday = getMaxday(month, year);
				if (maxday < day) { // illegal date
					if (_year > 0) {
						throw new ParseDateException();
					} else { // month-year case
						if (_hour <= 0) { // if no hour
							format = "mmm-yy";
						}
						year = day + 1900;
						day = 1;
					}
				}
			}
			int hour = parseHour(m);
			int minute = parseMinute(m);
			int second = parseSecond(m);
			int msecond = parseMillisecond(m);
			String ampm = parseAmpm(m);
			if (ampm != null) {
				// invalid hour, minute, second with am/pm
				if (hour > 12 || minute >= 60 || second >= 60) {
					throw new ParseDateException();
				}
				ampm = ampm.toUpperCase();
				if (hour < 12) {
					if (ampm.startsWith("P")) {
						hour += 12;
					}
				} else { // hour == 12
					if (ampm.startsWith("A")) {
						hour = 0;
					}
				}
			}
			if ((hour > 23 && minute >= 60) || (minute >= 60 && second >= 60) || (hour > 23 && second >= 60)) {
				throw new ParseDateException();
			}
			if (format == null) { // pure time pattern
				year = 1900;
				month = 1 - 1; // 0 based index
				day = 1;
				if (msecond >= 0) {
					format = "mm:ss.0";
				} else if (ampm != null) {
					format = second >= 0 ? "h:mm:ss AM/PM" : "h:mm AM/PM";
				} else if (hour < 24) {
					if (minute < 60 && second < 60) {
						format = second > 0 ? "h:mm:ss" : "h:mm";
					} // else a number; so format == null
				} else {
					if (minute < 60 && second < 60) {
						format = "[h]:mm:ss";
					} // else a number; so format == null
				}
			} else {
				if (msecond >= 0) {
					format = "mm:ss.0";
				} else if (hour > 23 || minute >= 60 || second >= 60) {
					format = null; // a number
				}
			}
			// date
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, month);
			cal.set(Calendar.DAY_OF_MONTH, day);
			// time
			if (hour < 0) {
				hour = 0;
			}
			if (minute < 0) {
				minute = 0;
			}
			if (second < 0) {
				second = 0;
			}
			if (msecond < 0) {
				msecond = 0;
			}
			cal.set(Calendar.HOUR_OF_DAY, hour);
			cal.set(Calendar.MINUTE, minute);
			cal.set(Calendar.SECOND, second);
			cal.set(Calendar.MILLISECOND, msecond);
			return cal.getTime();
		}
	}
}
