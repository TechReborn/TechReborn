import groovy.transform.TypeChecked

withConfig(configuration) {
    ast(TypeChecked, extensions: 'groovy.typecheckers.NullChecker')
}
