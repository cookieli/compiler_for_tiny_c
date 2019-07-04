.file "tests/codeGenOfMe/func_05.dcf"
.text
.section .rodata
.LC0:
.string "%d\n"
.LC1:
.string "return last number\n"
.LC2:
.string "%d\n"
.LC3:
.string "super max: %d\n"
.LC4:
.string "super max: %d\n"
.LC5:
.string "%d "
.LC6:
.string "\n"
.text
.globl max
.type max, @function
max:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
movq %rdi,-16(%rbp)

movq %rsi,-8(%rbp)

movq -16(%rbp),%rax
movq -8(%rbp),%r10
cmpq %r10,%rax
jle .L11
movq -16(%rbp),%r10
movq %r10,%rax
jmp .L6
jmp .L12
.L11:
movq -8(%rbp),%r10
movq %r10,%rax
jmp .L6
.L12:
nop
.L6:
leave
ret
.globl func
.type func, @function
func:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
movq $0,-8(%rbp)
.L13:
movb $1,%al
movb $0,%r10b
cmpb %r10b,%al
jle .L14
movq -8(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-8(%rbp)
movq -8(%rbp),%rax
cmpq $3,%rax
jne .L15
movq -8(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax

jmp .L7
jmp .L15
.L15:
nop
jmp .L13
.L14:
nop
.L7:
leave
ret
.globl superMax
.type superMax, @function
superMax:
pushq %rbp
movq %rsp, %rbp
subq $80, %rsp
movq %rdi,-65(%rbp)
movq %rsi,-57(%rbp)
movq %rdx,-49(%rbp)
movq %rcx,-41(%rbp)
movq %r8,-33(%rbp)
movq %r9,-25(%rbp)
movq 16(%rbp),%rax
movq %rax,-17(%rbp)
movq 24(%rbp),%rax
movq %rax,-9(%rbp)
movq 32(%rbp),%rax
movb %al,-1(%rbp)
movb -1(%rbp),%al
cmpb $0,%al
jle .L16
subq $64, %rsp
movq -57(%rbp),%rsi
movq -65(%rbp),%rdi
movq $0,%rax
call max
movq %rax,-121(%rbp)
movq -41(%rbp),%rsi
movq -49(%rbp),%rdi
movq $0,%rax
call max
movq %rax,-113(%rbp)
movq -25(%rbp),%rsi
movq -33(%rbp),%rdi
movq $0,%rax
call max
movq %rax,-105(%rbp)
movq -9(%rbp),%rsi
movq -17(%rbp),%rdi
movq $0,%rax
call max
movq %rax,-97(%rbp)
movq -113(%rbp),%rsi
movq -121(%rbp),%rdi
movq $0,%rax
call max
movq %rax,-89(%rbp)
movq -97(%rbp),%rsi
movq -105(%rbp),%rdi
movq $0,%rax
call max
movq %rax,-81(%rbp)
movq -81(%rbp),%rsi
movq -89(%rbp),%rdi
movq $0,%rax
call max
movq %rax,-73(%rbp)
movq -73(%rbp),%r10
movq %r10,%rax
jmp .L8
addq $64, %rsp
jmp .L17
.L16:
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq -9(%rbp),%r10
movq %r10,%rax
jmp .L8
.L17:
nop
.L8:
leave
ret
.globl fib
.type fib, @function
fib:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
movq %rdi,-8(%rbp)

movq -8(%rbp),%rax
cmpq $1,%rax
jne .L18
.L19:
movq $1,%rax
jmp .L9
jmp .L20
.L18:
movq -8(%rbp),%rax
cmpq $2,%rax
jne .L21
jmp .L19
.L21:
subq $48, %rsp
movq -8(%rbp),%rax
movq $1,%r10
subq %r10,%rax
movq %rax,-32(%rbp)
movq -32(%rbp),%rdi
movq $0,%rax
call fib
movq %rax,-40(%rbp)
movq -8(%rbp),%rax
movq $2,%r10
subq %r10,%rax
movq %rax,-16(%rbp)
movq -16(%rbp),%rdi
movq $0,%rax
call fib
movq %rax,-24(%rbp)
movq -40(%rbp),%rax
movq -24(%rbp),%r10
addq %r10,%rax
movq %rax,-48(%rbp)
movq -48(%rbp),%r10
movq %r10,%rax
jmp .L9
addq $48, %rsp
.L20:
nop
.L9:
leave
ret
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $48, %rsp
movq $4,-48(%rbp)
movq $5,-40(%rbp)
movq -40(%rbp),%rsi
movq -48(%rbp),%rdi
movq $0,%rax
call max
movq %rax,-24(%rbp)
movq -24(%rbp),%rsi
leaq .LC2(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
subq $8,%rsp
movq $1,%rdi
pushq %rdi
movq $8,%rdi
pushq %rdi
movq $7,%rdi
pushq %rdi
movq $6,%r9
movq $5,%r8
movq $4,%rcx
movq $3,%rdx
movq $2,%rsi
movq $1,%rdi
movq $0,%rax
call superMax
movq %rax,-16(%rbp)
addq $32,%rsp
movq -16(%rbp),%rsi
leaq .LC3(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
subq $8,%rsp
movq $0,%rdi
pushq %rdi
movq $8,%rdi
pushq %rdi
movq $7,%rdi
pushq %rdi
movq $6,%r9
movq $5,%r8
movq $4,%rcx
movq $3,%rdx
movq $2,%rsi
movq $1,%rdi
movq $0,%rax
call superMax
movq %rax,-8(%rbp)
addq $32,%rsp
movq -8(%rbp),%rsi
leaq .LC4(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $0,%rax
call func
movq $0,%rax
movq $1,-32(%rbp)
.L22:
movq -32(%rbp),%rax
cmpq $50,%rax
jge .L23
subq $16, %rsp
movq -32(%rbp),%rdi
movq $0,%rax
call fib
movq %rax,-56(%rbp)
movq -56(%rbp),%rsi
leaq .LC5(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq -32(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-32(%rbp)
addq $16, %rsp
jmp .L22
.L23:
leaq .LC6(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
.L10:
leave
ret

